package me.zingle.android_sdk;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

import me.zingle.android_sdk.daemons.MessageSender;
import me.zingle.android_sdk.facade_models.Attachment;
import me.zingle.android_sdk.facade_models.Message;
import me.zingle.android_sdk.model_view.DataServices;
import me.zingle.android_sdk.utils.Client;

public class ZingleMessagingActivity extends AppCompatActivity {

    public static final String BASE_SERVICE_ID="base_service_id";

    private MessagesList messagesList;
    private MessageComposer messageComposer;
    private Client.ConversationClient client;

    public static final int REQUEST_CODE_GALLERY = 111;
    public static final int REQUEST_CODE_CAMERA = 112;


    public Client.ConversationClient getClient() {
        return client;
    }

    public void setClient(Client.ConversationClient client) {
        this.client = client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zingle_messaging_activity);

        String incomingService=getIntent().getStringExtra(BASE_SERVICE_ID);

        client =Client.getItem().getClient(incomingService);

        client.setListVisible(true);

        messagesList = (MessagesList) findViewById(R.id.atlas_screen_messages_messages_list);


        final DataServices dataGroupServices = DataServices.getItem();

        messageComposer = (MessageComposer) findViewById(R.id.atlas_screen_messages_message_composer);
        messageComposer.init(client);
        messageComposer.setListener(new MessageComposer.Listener() {
            public boolean beforeSend(Message message) {

                dataGroupServices.addItem(message);
                dataGroupServices.addToConversation(client.getConnectedService().getId(),message);
                dataGroupServices.updateMessagesList();

                if(messagesList!=null){
                    messagesList.reloadMessagesList();
                    messagesList.showLastMessage();
                }

                Intent sendIntent=new Intent(messageComposer.getContext(), MessageSender.class);
                sendIntent.putExtra(Message.SEND_INTENT_MSG_ID, message.getId());

                return startService(sendIntent) != null;
            }

        });

        messageComposer.registerMenuItem("Photo", new View.OnClickListener() {
            public void onClick(View v) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                }
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
    protected void onStop() {
        client.setListVisible(false);
        super.onStop();
    }

    @Override
    protected void onStart() {
        client.setListVisible(true);
        messagesList.init(this);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zingle_messaging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Attachment att = null;

            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    if (data.getData() != null) {
                        att = new Attachment();
                        ContentResolver cR = this.getContentResolver();
                        String type = cR.getType(data.getData());
                        att.setMimeType(type);

                        InputStream stream = null;
                        UUID cachePath = UUID.randomUUID();

                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        if (imageBitmap != null) {
                            Bitmap.CompressFormat format;
                            format = Bitmap.CompressFormat.JPEG;

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            imageBitmap.compress(format, 100, bos);

                            DataServices dataServices = DataServices.getItem();

                            dataServices.addCachedItem(cachePath.toString(), bos.toByteArray());

                            att.setUri(null);
                            att.setCachePath(cachePath.toString());
                            att.setTextContent("Image from camera.\n" + att.getUri());
                        }
                    }

                    break;
                case REQUEST_CODE_GALLERY:
                    if (data != null) {
                        att = new Attachment();
                        ContentResolver cR = this.getContentResolver();
                        String type = cR.getType(data.getData());

                        att.setMimeType(type);

                        att.setUri(data.getData());
                        att.setCachePath(data.getData().toString());
                        att.setTextContent("Image from gallery.\n" + att.getUri());
                    }
                    break;

                default:
                    break;
            }
            if (att != null) messageComposer.createdMessage.addAttachment(att);
            messageComposer.updateAttachmentsList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}