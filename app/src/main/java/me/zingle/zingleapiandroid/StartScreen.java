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

import java.util.List;

import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.logger.ZingleVerbosityLevel;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.services.ZingleContactServices;
import me.zingle.api.sdk.services.ZingleServiceServices;
import me.zingle.atlas_adoption.ZingleMessagingActivity;
import me.zingle.atlas_adoption.daemons.MessageReceiver;
import me.zingle.atlas_adoption.daemons.WorkingDataSet;
import me.zingle.atlas_adoption.utils.Client;

//import android.util.Log;

public class StartScreen extends AppCompatActivity {
    final String name="viacheslav.marusyk@cyberhull.com";
    final String password="20cheVrolet15";
    final String serviceId="b1037b83-f2b6-4258-9b62-655c2478a329";
    final String contactId="e68a7c1e-fb5c-4b20-a68e-aca5731399fc";
    final String serviceChannelTypeId="7c3b4034-f4df-4b85-9617-7571c46d7388";

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

            publishProgress("\nAllowed services:\n");

            ZingleList<ZingleService> serviceList=serviceServices.list();
            if(serviceList!=null && serviceList.objects.size()>0) {
                wds.setAllowedServices(serviceList.objects);

                publishProgress(wds.getAllowedServices().toString());
                return true;
            }
            else
                return false;
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

    private class SetUpContact extends AsyncTask<Integer,String,Boolean> {
        TextView displayText=null;

        @Override
        protected void onPreExecute() {
            displayText=(TextView) findViewById(R.id.start_screen_text);
            displayText.setText("\nRegistering contact...");        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            ZingleContactServices contactServices=new ZingleContactServices(wds.getAllowedServices().get(params[0]));


            //TODO Temporary contact GET substitution
            List<QueryPart> conditions=contactServices.createConditions("channel_value", "*044*");
            ZingleList<ZingleContact> contacts=contactServices.list(conditions);

            if(contacts!=null && contacts.objects.size()>0){
                publishProgress("\nAssigning contact...");
                wds.addContacts(contacts.objects);
                publishProgress(contacts.toString());

                Intent receiveIntent=new Intent(getBaseContext(), MessageReceiver.class);
                //receiveIntent.putExtra(Message.RECEIVE_INTENT_CONTACT_ID,contact.getId());
                startService(receiveIntent);

                return true;
            }
            else
                return false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            displayText.append(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                displayText.append("\nContact assigned.");
                continueApp.setClickable(true);


            }
            else
                displayText.append("\nContact assignment failed.");
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
                    intent.putExtra("Token", wds.getLogin());
                    intent.putExtra("Key", wds.getPassword());
                    intent.putExtra(Client.CONTACT_ID, wds.getContact(0).getId());
                    intent.putExtra(Client.CONTACT_CH_VALUE, wds.getContact(0).getChannels().get(0).getValue());
                    intent.putExtra(Client.SERVICE_ID, service.getId());
                    intent.putExtra(Client.SERVICE_NAME, service.getDisplayName());
                    intent.putExtra(Client.SERVICE_CH_VALUE, service.getChannels().get(0).getValue());
                    intent.putExtra(Client.CH_TYPE_ID, service.getChannelTypes().get(0).getId());

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

            new SetUpContact().execute(id);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
