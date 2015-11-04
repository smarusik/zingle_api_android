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

    /**
     * Initializes basic parameters for connecting to API. Doesn't make any verification (it means true answer doesn't mean that login and password are correct and URL is working).
     *
     * @param apiURL URL, that give access to API
     * @param apiVersion API version string in form v{number}
     * @param token user name for API
     * @param password password for API
     * @return true if apiURL meets URL standards, otherwise - false
     */
    public static boolean initializeConnection(String apiURL, String apiVersion, String token, String password){

        if(!ZingleConnection.init(apiURL, apiVersion, token, password)) {
            Log.err("Wrong API URL or version.");
            return false;
        }
        else {
            return true;
        }
    }

    /**
     *Base class used in addConversation(String, String, String, ConversationAdderBase) for adding conversation. Allows to
     *customize the process through overloading onPreExecute(), onPostExecute(Boolean aBoolean), onProgressUpdate(String... values) and others.
     *For example, one can show some progress bar by overloading onProgressUpdate() and show or activate some UI elements (buttons,
     *menu items etc.) by overloading onPostExecute(Boolean aBoolean). This class inherits AsyncTask<String,String,Boolean> so there you can look
     *for more information on overloadable functions.
     */
    public static abstract class ConversationAdderBase extends AsyncTask<String,String,Boolean>{
        String serviceId;
        String contactId;
        String contactChannelValue;

        @Override
        protected final Boolean doInBackground(String... params) {
            serviceId=params[0];
            contactId=params[1];
            contactChannelValue=params[2];

            ZingleServiceServices serviceServices=new ZingleServiceServices();

            WorkingDataSet wds=WorkingDataSet.getItem();

            //Assign service and it's contact
            ZingleService service=serviceServices.get(serviceId);
            if(service==null){
                publishProgress("1", "Failed");
                Log.err("Failed to add conversation for service with id=%s",serviceId);
                return false;
            }
            wds.addAllowedService(service);
            publishProgress("1",service.getDisplayName());

            ZingleContactServices contactServices=new ZingleContactServices(service);
            ZingleContact contact=contactServices.get(contactId);
            if(contact==null){
                publishProgress("2", "Failed");
                Log.err("Failed to add conversation for contact with id=%s",contactId);
                return false;
            }
            wds.addContact(contact);
            publishProgress("2",contact.getId());

            //Registering conversation UI data
            Client.ConversationClient client=new Client.ConversationClient();

            /*
            ChannelTypeClass field value of ZingleChannelType, that will be used for sending messages
            (see ZingleNewMessage.channelTypes)
            */
            String allowedChannelTypeClass = "UserDefinedChannel";

            //Check for proper channel type
            for(ZingleChannelType t:service.getChannelTypes()) {
                if(t.getTypeclass().equals(allowedChannelTypeClass)) {
                    client.addChannelTypeId(t.getId());
                    break;
                }
            }
            if(client.getChannelTypeId().isEmpty()) {
                publishProgress("3", "Failed");
                Log.err("Service %s (id=%s) does not support user defined channels.",service.getDisplayName(),service.getId());
                return false;
            }
            publishProgress("3", allowedChannelTypeClass);

            //Assign conversation participants.
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

            Client.getItem().addClient(client);

            publishProgress("4", "UI ready");

            return true;
        }
    }

    /**
     *Built in implementation of ConversationAdderBase. Used in addConversation(String, String, String) to provide basic
     * conversation registration functional. Writes all progress to Log, does not interact with UI.
     */
    public static abstract class ConversationAdder extends ConversationAdderBase{
        String serviceId;
        String contactId;
        String contactChannelValue;

        @Override
        protected void onPreExecute() {
            Log.info("\nTrying to initialize conversation.");
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
    }

    /**
     * Registers a new conversation in zingle_android_sdk. If succeed, system begins to receive all messages for ZingleContact with specified ID
     * and it's possible to open UI for this conversation with showConversation().
     *
     * @param serviceId ID of service, which participate in conversation
     * @param contactId ID of contact, which participate in conversation
     * @param contactChannelValue contact channel value for sending messages to service (see ZingleNewMessage.correspondent.channel.value)
     */
    public static void addConversation(String serviceId, String contactId, String contactChannelValue){

        new ConversationAdder(){}.execute(serviceId, contactId, contactChannelValue);
    }

    /**
     * Same as addConversation(String serviceId, String contactId, String contactChannelValue), but allow to customize the process
     * through overloading <i>onPreExecute(), onPostExecute(Boolean aBoolean), onProgressUpdate(String... values)</i>.
     * See <i>ConversationAdder</i> and <i>AsyncTask<Params,Progress,Result></i> for more information.
     *<br>
     * onProgressUpdate(String... values) is triggered 4 times:<br>
     * <bl>
     * <li>After registering service: values "1" and service's DisplayName (see <i>ZingleService</i>) or "Failed"</li>
     * <li>After registering contact: values "2" and contact's id (see <i>ZingleContact</i>) of "Failed"</li>
     * <li>After trying to find proper channel type: values "3" and allowed channel typeclass (see <i>ZingleChannelType</i>) or "Failed"</li>
     * <li>After registering conversation participants: values "4" and "UI ready".</li>
     * </bl>
     *
     * @param serviceId ID of service, which participate in conversation
     * @param contactId ID of contact, which participate in conversation
     * @param contactChannelValue contact channel value for sending messages to service (see ZingleNewMessage.correspondent.channel.value)
     * @param ca customized implementation of ConversationAdderBase
     */
    public static void addConversation(String serviceId, String contactId, String contactChannelValue, ConversationAdderBase ca){
        ca.execute(serviceId, contactId, contactChannelValue);
    }

    /**
     * Starts message receiving for all registered conversations. Conversations may be added seamlessly before and after triggering this function.
     * @param context current application context (can be get in activity by getApplicationContext())
     */
    public static void startMessageReceiver(Context context){
        Intent receiveIntent=new Intent(context, MessageReceiver.class);
        context.startService(receiveIntent);
    }


    /**
     * Starts and shows the UI for required conversation (defined by <b>serviceId</b>)
     * @param context current context (in most cases can be get as <b>this</b> if function runs in activity)
     * @param serviceId the ID of service to show conversation with
     */
    public static void showConversation(Context context,String serviceId){
        Client client=Client.getItem();

        if(client.getClient(serviceId)!=null) {
            Intent intent = new Intent(context, ZingleMessagingActivity.class);
            intent.putExtra(ZingleMessagingActivity.BASE_SERVICE_ID, serviceId);
            context.startActivity(intent);
        }
    }
}