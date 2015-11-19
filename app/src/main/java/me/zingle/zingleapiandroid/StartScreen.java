package me.zingle.zingleapiandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import me.zingle.android_sdk.ZingleUIInitAndStart;
import me.zingle.android_sdk.daemons.WorkingDataSet;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.logger.ZingleVerbosityLevel;
import me.zingle.api.sdk.model.ZingleService;

public class StartScreen extends AppCompatActivity {
    final String name="viacheslav.marusyk@cyberhull.com";
    final String password="123qweasd";
    final String contactChannelValue="viacheslav.marusyk";
    final String[] contactIds={"8120ea45-1af8-4948-9b82-5e69de0bd4cb","cb23cba5-e9de-4eaa-b1c9-85ed56f8b287"};
    final String[] serviceIds={"e84bec95-b788-45ea-9d64-01db3d8742ac","b1037b83-f2b6-4258-9b62-655c2478a329"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        final TextView logText=(TextView) findViewById(R.id.start_screen_text);

        Log.init(ZingleVerbosityLevel.ZINGLE_VERBOSITY_INFO, System.err);

        //Stage 1 init connection
        if(ZingleUIInitAndStart.initializeConnection("https://qa-api.zingle.me", "v1", name, password)) {

            //Stage 2 init conversations
            //First
            ZingleUIInitAndStart.addConversation(serviceIds[0],
                    contactIds[0],
                    contactChannelValue,
                    new ZingleUIInitAndStart.ConversationAdderBase() {
                        @Override
                        protected void onPreExecute() {
                            logText.append("\nAdding conversation 1.");
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            if(aBoolean) logText.append("Conversation 1 added.");
                            else logText.append("\nConversation 1 failed.");
                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            logText.append("\n"+values[0]+" "+values[1]);
                        }
                    });
            //Second
            /*ZingleUIInitAndStart.addConversation(serviceIds[1],
                    contactIds[1],
                    contactChannelValue,
                    new ZingleUIInitAndStart.ConversationAdderBase() {
                        @Override
                        protected void onPreExecute() {
                            logText.append("\nAdding conversation 2.");
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            if(aBoolean) logText.append("Conversation 2 added.");
                            else logText.append("\nConversation 2 failed.");
                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            logText.append("\n"+values[0]+" "+values[1]);
                        }
                    });*/
            //Stage 3 start message receiver
            ZingleUIInitAndStart.startMessageReceiver(getApplicationContext());

        }
        else{
            logText.setText("\nWrong API URL.");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        WorkingDataSet wds=WorkingDataSet.getItem();

        for(int i=0;i<wds.getAllowedServices().size();i++) {
            MenuItem it=menu.findItem(i);
            if(it==null)
                menu.add(Menu.NONE, i, Menu.NONE, wds.getAllowedServices().get(i).getDisplayName());
            else
                it.setTitle( wds.getAllowedServices().get(i).getDisplayName());
        }

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
        int id = item.getItemId();
        WorkingDataSet wds=WorkingDataSet.getItem();

        if (id < wds.getAllowedServices().size()) {
            ZingleService service=wds.getAllowedServices().get(id);

            //Activate required UI
            ZingleUIInitAndStart.showConversation(this, service.getId());
        }

        return super.onOptionsItemSelected(item);
    }
}
