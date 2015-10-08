package me.zingle.atlas_adoption;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import me.zingle.atlas_adoption.daemons.MessageSender;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.facade_models.Participant;
import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.utils.Client;

public class ZingleMessagingActivity extends AppCompatActivity {

    private MessagesList messagesList;
    private MessageComposer messageComposer;

    /**
     * used to take photos from camera
     */
    private File photoFile = null;

    public static final int REQUEST_CODE_SETTINGS = 101;
    public static final int REQUEST_CODE_GALLERY = 111;
    public static final int REQUEST_CODE_CAMERA = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zingle_messaging_activity);
        Client client = new Client();

        Bundle credentials = getIntent().getExtras();
        if (credentials != null) {
            /*Toast.makeText(this.getApplicationContext(),
                    "Token="+credentials.getString("Token")+"\nKey="+credentials.getString("Key")+"\nID: "+credentials.getLong("contactId")
                    ,Toast.LENGTH_LONG).show();*/

            client.setAuthContact(new Participant(credentials.getString(Client.CONTACT_NAME),
                    Participant.ParticipantType.CONTACT,
                    credentials.getString(Client.CONTACT_ID),
                    credentials.getString(Client.CONTACT_CH_TYPE),
                    credentials.getString(Client.CONTACT_CH_VALUE)));


            client.setConnectedService(new Participant(credentials.getString(Client.SERVICE_NAME),
                    Participant.ParticipantType.SERVICE,
                    credentials.getString(Client.SERVICE_ID),
                    credentials.getString(Client.SERVICE_CH_TYPE),
                    credentials.getString(Client.SERVICE_CH_VALUE)));

        }

        messagesList = (MessagesList) findViewById(R.id.atlas_screen_messages_messages_list);

        messagesList.init(this, client);
        final DataServices dataGroupServices = DataServices.getItem();

        messageComposer = (MessageComposer) findViewById(R.id.atlas_screen_messages_message_composer);
        messageComposer.init(client);
        messageComposer.setListener(new MessageComposer.Listener() {
            public boolean beforeSend(Message message) {

                dataGroupServices.addItem(message,messagesList);
                dataGroupServices.addUnsentMessage(message);

                Intent sendIntent=new Intent(messageComposer.getContext(), MessageSender.class);

                startService(sendIntent);

                return true;
            }

        });

        messageComposer.registerMenuItem("Photo", new View.OnClickListener() {
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = "cameraOutput" + System.currentTimeMillis() + ".jpg";
                photoFile = new File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), fileName);
                final Uri outputUri = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
            }
        });

        messageComposer.registerMenuItem("Image", new View.OnClickListener() {
            public void onClick(View v) {
                // in onCreate or any event where your want the user to select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zingle_messaging, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {

            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) return;

        Attachment att=null;

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if(data!=null) {
                    att = new Attachment();
                    ContentResolver cR = this.getContentResolver();
                    String type = cR.getType(data.getData());

                    att.setMimeType(type);

                    att.setUri(data.getData());
                    att.setTextContent("Image from camera.\n" + att.getLocalPath());
                }

                break;
            case REQUEST_CODE_GALLERY:
                if(data!=null) {
                    att = new Attachment();
                    ContentResolver cR = this.getContentResolver();
                    String type = cR.getType(data.getData());

                    att.setMimeType(type);

                    att.setUri(data.getData());
                    att.setTextContent("Image from gallery.\n" + att.getLocalPath());
                }
               break;

            default:
                break;
        }
        if(att!=null) messageComposer.createdMessage.addAttachment(att);
    }

}