package me.zingle.atlas_adoption;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.model.ZingleChannelType;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.services.ZingleContactServices;
import me.zingle.api.sdk.services.ZingleServiceServices;
import me.zingle.atlas_adoption.daemons.MessageReceiver;
import me.zingle.atlas_adoption.daemons.WorkingDataSet;
import me.zingle.atlas_adoption.facade_models.Participant;
import me.zingle.atlas_adoption.utils.Client;

/**
 * Created by SLAVA 11 2015.
 */
public class ZingleUIInitAndStart {

    private static String allowedChannelTypeClass="UserDefinedChannel";

    public static boolean initializeConnection(String apiURL, String apiVersion, String token, String password){

        if(!ZingleConnection.init(apiURL, apiVersion, token, password)) {
            Log.err("Wrong API URL or version.");
            return false;
        }
        else {
            return true;
        }
    }

    public static <ViewType> void addConversation(String serviceId, String contactId, String contactChannelValue, ViewType viewForUpdate){

        new AsyncTask<String,String,Boolean>(){

            String serviceId;
            String contactId;
            String contactChannelValue;

            @Override
            protected void onPreExecute() {
                Log.info("\nTrying to initialize conversation.");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                serviceId=params[0];
                contactId=params[1];
                contactChannelValue=params[2];

                ZingleServiceServices serviceServices=new ZingleServiceServices();

                Client clients=Client.getItem();
                WorkingDataSet wds=WorkingDataSet.getItem();

                //Assign 1st service and it's contact
                ZingleService service=serviceServices.get(serviceId);
                wds.addAllowedService(service);

                publishProgress(service.getDisplayName());

                ZingleContactServices contactServices=new ZingleContactServices(service);
                ZingleContact contact=contactServices.get(contactId);

                wds.addContact(contact);

                publishProgress(contact.getId());

                Client.ConversationClient client=new Client.ConversationClient();

                Participant p=new Participant();
                p.setType(Participant.ParticipantType.CONTACT);
                p.setId(contactId);
                p.setChannelValue(contactChannelValue);
                client.setAuthContact(p);

                p=new Participant();
                p.setType(Participant.ParticipantType.SERVICE);
                p.setId(service.getId());
                p.setName(service.getDisplayName());
                client.setConnectedService(p);

                for(ZingleChannelType t:service.getChannelTypes()) {
                    if(t.getTypeclass().equals(allowedChannelTypeClass)) {
                        client.addChannelTypeId(t.getId());
                        break;
                    }
                }
                if(client.getChannelTypeId().isEmpty()) {
                    Log.err("Service %s (id=%s) does not support user defined channels.",service.getDisplayName(),service.getId());
                    return false;
                }

                clients.addClient(client);

                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                final String result="Conversation between service id=%s and contact id=%s %s.";
                if(aBoolean){
                    Log.info(result,serviceId,contactId,"initialized");
                }
                else{
                    Log.err(result, serviceId, contactId, "failed");
                }
            }

            @Override
            protected void onProgressUpdate(String... values) {
                for(int i=0;i<values.length;i++) {
                    Log.info(values[i]);
                }
            }

        }.execute(serviceId, contactId, contactChannelValue);
    }

    public static void startMessageReceiver(Context context){
        Intent receiveIntent=new Intent(context, MessageReceiver.class);
        context.startService(receiveIntent);
    }

    public static void showConversation(Context context,String serviceId){
        Intent intent = new Intent(context, ZingleMessagingActivity.class);
        intent.putExtra(ZingleMessagingActivity.BASE_SERVICE_ID, serviceId);
        context.startActivity(intent);

    }
}