package me.zingle.zingleapiandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.logger.ZingleVerbosityLevel;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.atlas_adoption.ZingleUIInitAndStart;
import me.zingle.atlas_adoption.daemons.WorkingDataSet;

//import android.util.Log;

public class StartScreen extends AppCompatActivity {
    final String name="viacheslav.marusyk@cyberhull.com";
    final String password="20cheVrolet15";
    final String[] contactIds={"19197565-6767-4110-951b-610bc7e362fb","27e4198e-1f07-414c-beef-5094916e56c1"};
    final String[] serviceIds={"4bc8bb76-0d19-48ff-815a-14e950fc776a","b1037b83-f2b6-4258-9b62-655c2478a329"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Log.init(ZingleVerbosityLevel.ZINGLE_VERBOSITY_INFO, System.err);

        if(ZingleUIInitAndStart.initializeConnection("https://qa3-api.zingle.me", "v1", name, password)) {

            ZingleUIInitAndStart.ConversationAdding ca=new ZingleUIInitAndStart.ConversationAdding<TextView>(serviceIds[0],
                                                                                                        contactIds[0],
                                                                                                        "viacheslav.marusyk",
                                                                            (TextView) findViewById(R.id.start_screen_text)){

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);

                    viewForUpdate.append("\n");
                    for(String s:values) viewForUpdate.append(s);
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);

                    viewForUpdate.append("Conversation 1 added.");
                }
            };

            ca.execute(serviceIds[0]);

            ca=new ZingleUIInitAndStart.ConversationAdding<TextView>(serviceIds[1],
                    contactIds[1],
                    "viacheslav.marusyk",
                    (TextView) findViewById(R.id.start_screen_text)){

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);

                    viewForUpdate.append("\n");
                    for(String s:values) viewForUpdate.append(s);
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);

                    viewForUpdate.append("Conversation 2 added.");
                }
            };

            ca.execute(serviceIds[1]);

            ZingleUIInitAndStart.startMessageReceiver(getApplicationContext());

        }
        else{
            TextView startScreen=(TextView) findViewById(R.id.start_screen_text);
            startScreen.setText("Illegal credentials:\nToken=" + name + "\nKey=" + password + "\n");
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        WorkingDataSet wds=WorkingDataSet.getItem();

        //noinspection SimplifiableIfStatement
        if (id < wds.getAllowedServices().size()) {

            ZingleService service=wds.getAllowedServices().get(id);

            ZingleUIInitAndStart.showConversation(this, service.getId());
        }

        return super.onOptionsItemSelected(item);
    }


}
