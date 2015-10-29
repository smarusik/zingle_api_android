package me.zingle.zingleapiandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.logger.ZingleVerbosityLevel;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.services.ZingleContactServices;
import me.zingle.api.sdk.services.ZingleServiceServices;
import me.zingle.atlas_adoption.ZingleMessagingActivity;
import me.zingle.atlas_adoption.daemons.MessageReceiver;
import me.zingle.atlas_adoption.daemons.WorkingDataSet;
import me.zingle.atlas_adoption.facade_models.Participant;
import me.zingle.atlas_adoption.utils.Client;

//import android.util.Log;

public class StartScreen extends AppCompatActivity {
    final String name="viacheslav.marusyk@cyberhull.com";
    final String password="20cheVrolet15";
    final String[] contactIds={"19197565-6767-4110-951b-610bc7e362fb","27e4198e-1f07-414c-beef-5094916e56c1"};
    final String[] serviceIds={"4bc8bb76-0d19-48ff-815a-14e950fc776a","b1037b83-f2b6-4258-9b62-655c2478a329"};

    WorkingDataSet wds;
    Button continueApp;
    ZingleService service=null;

    private class GetAllowedServices extends AsyncTask<String,String,Boolean>{

        TextView displayText=null;

        @Override
        protected void onPreExecute() {
            displayText=(TextView) findViewById(R.id.start_screen_text);
            displayText.setText("\nWait please...");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ZingleServiceServices serviceServices=new ZingleServiceServices();

            Client clients=Client.getItem();

            publishProgress("\nAllowed services:\n");

            //Assign 1st service and it's contact
            ZingleService service=serviceServices.get(serviceIds[0]);
            wds.addAllowedService(service);
            publishProgress(wds.getAllowedServices().toString());

            ZingleContactServices contactServices=new ZingleContactServices(service);
            ZingleContact contact=contactServices.get(contactIds[0]);

            publishProgress("\nAssigning contact...");
            List<ZingleContact> contacts=new ArrayList<ZingleContact>();
            contacts.add(contact);
            publishProgress(contact.toString());

            Client.ConversationClient client=new Client.ConversationClient();

            client.setToken(name);
            client.setKey(password);

            Participant p=new Participant();
            p.setType(Participant.ParticipantType.CONTACT);
            p.setId(contactIds[0]);
            p.setChannelValue("+15152935566");
            client.setAuthContact(p);

            p=new Participant();
            p.setType(Participant.ParticipantType.SERVICE);
            p.setId(service.getId());
            p.setName(service.getDisplayName());
            p.setChannelValue(service.getChannels().get(0).getValue());
            client.setConnectedService(p);
            client.addChannelTypeId(service.getChannels().get(0).getType().getId());
            clients.addClient(client);

            //Assign 2nd service and it's contact
            service=serviceServices.get(serviceIds[1]);
            wds.addAllowedService(service);
            publishProgress(wds.getAllowedServices().toString());

            contactServices=new ZingleContactServices(service);
            contact=contactServices.get(contactIds[1]);

            publishProgress("\nAssigning contact...");
            contacts.add(contact);
            publishProgress(contact.toString());

            client=new Client.ConversationClient();

            client.setToken(name);
            client.setKey(password);

            p=new Participant();
            p.setType(Participant.ParticipantType.CONTACT);
            p.setId(contactIds[1]);
            p.setChannelValue("+15152935566");
            client.setAuthContact(p);

            p=new Participant();
            p.setType(Participant.ParticipantType.SERVICE);
            p.setId(service.getId());
            p.setName(service.getDisplayName());
            p.setChannelValue(service.getChannels().get(0).getValue());
            client.setConnectedService(p);
            client.addChannelTypeId(service.getChannels().get(0).getType().getId());
            clients.addClient(client);

            wds.setContacts(contacts);
            Intent receiveIntent=new Intent(getBaseContext(), MessageReceiver.class);
            startService(receiveIntent);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){

                displayText.append("\nServices ready.");
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            displayText.append("\n"+values[0]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Log.init(ZingleVerbosityLevel.ZINGLE_VERBOSITY_INFO, System.err);

        wds =WorkingDataSet.getItem();
        wds.setLogin(name);
        wds.setPassword(password);

        if(ZingleConnection.init("https://qa3-api.zingle.me", "v1", wds.getLogin(), wds.getPassword())) {


            new GetAllowedServices().execute();

            continueApp=(Button) findViewById(R.id.continue_button);
            continueApp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getBaseContext(), ZingleMessagingActivity.class);
                    intent.putExtra(ZingleMessagingActivity.BASE_SERVICE_ID, service.getId());

                    startActivity(intent);

                }
            });
            continueApp.setClickable(false);

        }
        else{
            TextView startScreen=(TextView) findViewById(R.id.start_screen_text);
            startScreen.setText("Illegal credentials:\nToken=" + name + "\nKey=" + password + "\n");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_app, menu);

        for(int i=0;i<wds.getAllowedServices().size();i++) {
            MenuItem it=menu.findItem(i);
            if(it==null)
                menu.add(Menu.NONE, i, Menu.NONE, wds.getAllowedServices().get(i).getDisplayName());
            else
                it.setTitle( wds.getAllowedServices().get(i).getDisplayName());
        }

        //menu.removeItem(R.id.action_zingle);

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 0, Menu.NONE, "Empty");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id < wds.getAllowedServices().size()) {

            //Here channel types and values must be defined somehow.

            service=wds.getAllowedServices().get(id);
            continueApp.setClickable(false);
            Intent intent = new Intent(getBaseContext(), ZingleMessagingActivity.class);
            intent.putExtra(ZingleMessagingActivity.BASE_SERVICE_ID, service.getId());
            startActivity(intent);


            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
