package me.zingle.atlas_adoption.model_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zingle.atlas_adoption.R;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.utils.Client;

import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_UNSUPPORTED;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleListAdapter extends BaseExpandableListAdapter {

    public final String[] TIME_WEEKDAYS_NAMES  /*=new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}*/;
    public final int TIME_HOURS_24 = 24 * 60 * 60 * 1000;
    public final DateFormat timeFormat;

    private static final float CELL_CONTAINER_ALPHA_UNSENT  = 0.5f;
    private static final float CELL_CONTAINER_ALPHA_SENT    = 1.0f;

    private DataServices dataGroupServices=DataServices.getItem();
    public LayoutInflater layoutInflater;
    public Activity activity;
    //private Context context;
    private final Client client;

    private final MessageListStyleSettings styleSettings;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    public static final SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat("EEE, LLL dd,", Locale.getDefault());

    public ZingleListAdapter(Activity activity, Client client, MessageListStyleSettings styleSettings) {
        this.activity = activity;
        this.client = client;
        this.styleSettings = styleSettings;
        Context context=activity.getApplication().getApplicationContext();
        this.layoutInflater = activity.getLayoutInflater();
        TIME_WEEKDAYS_NAMES=context.getResources().getStringArray(R.array.weekdays_names);
        timeFormat=android.text.format.DateFormat.getTimeFormat(context);




        //Example data
        Date today=new Date();

        dataGroupServices.addItem(new Message("Message 2_1", client.getAuthContact(), client.getConnectedService(), new Date(today.getTime()-86401000)));
        dataGroupServices.addItem(new Message("Message 2_2", client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-86403000)));
        dataGroupServices.addItem(new Message("Message 2_3 And it would be a veeeeeeeery long message in order to fill space from left to right.....",
                client.getAuthContact(), client.getConnectedService(), new Date(today.getTime()-86402000)));

        dataGroupServices.addItem(new Message("Message 1_1", client.getAuthContact(), client.getConnectedService(), new Date(today.getTime()-1)));
        dataGroupServices.addItem(new Message("Message 1_2", client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-2)));

        Message msg=new Message("Message 1_3 It's a very loooong message too. See how it looks like.",
                client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-3));
        Attachment att=new Attachment();

        att.setMimeType(MIME_TYPE_UNSUPPORTED);
        att.setUri(Uri.parse("https://qa3-dashboard.zingle.me/service/612/contacts"));
        att.setTextContent("URL attached");

        msg.addAttachment(att);

        dataGroupServices.addItem(msg);


        dataGroupServices.addItem(new Message("Message 1_4 Continue to fill all the space with messages from both sides.",
                client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-4)));

        dataGroupServices.addItem(new Message("Message 4_1", client.getAuthContact(), client.getConnectedService(), new Date(today.getTime()-86400000*5+1000)));
        dataGroupServices.addItem(new Message("Message 4_2", client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-86400000*5+4000)));
        dataGroupServices.addItem(new Message("Message 4_3 It's a very loooong message too. See how it looks like.",
                client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-86400000*5+2000)));
        dataGroupServices.addItem(new Message("Message 4_4 Continue to fill all the space with messages from both sides.",
                client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-86400000*5+3000)));

        dataGroupServices.addItem(new Message("Message 3_4", client.getAuthContact(), client.getConnectedService(), new Date(today.getTime()-86400000*2+1000)));
        dataGroupServices.addItem(new Message("Message 3_1", client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-86400000*2+4000)));
        dataGroupServices.addItem(new Message("Message 3_3 Another day with veeeeeeeery long message in order to fill space from left to right.....",
                client.getAuthContact(), client.getConnectedService(), new Date(today.getTime()-86400000*2+2000)));
        dataGroupServices.addItem(new Message("Message 3_2", client.getConnectedService(), client.getAuthContact(), new Date(today.getTime()-86400000*2+3000)));

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataGroupServices.getItemData(groupPosition,childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Message msg=dataGroupServices.getItemData(groupPosition,childPosition);

        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.atlas_view_messages_convert,null);
        }

        /*spacerTop always present for now so left as is*/
        //View spacerTop = convertView.findViewById(R.id.atlas_view_messages_convert_spacer_top);

        /*We have only one counterpart in conversation, so there is no need to emphasize it name all the time*/
        TextView userNameHeader = (TextView) convertView.findViewById(R.id.atlas_view_messages_convert_user_name);
        userNameHeader.setVisibility(View.GONE);

        /*Only text avatar support for now*/
        View avatarContainer = convertView.findViewById(R.id.atlas_view_messages_convert_avatar_container);
        TextView textAvatar = (TextView) convertView.findViewById(R.id.atlas_view_messages_convert_initials);
        //ImageView avatarImgView = (ImageView) convertView.findViewById(R.id.atlas_view_messages_convert_avatar_img);

        /*Show avatar only for counterpart*/
        if(msg.getSender().equals(client.getConnectedService())) {
            avatarContainer.setVisibility(View.VISIBLE);
            textAvatar.setVisibility(View.VISIBLE);
            textAvatar.setText(client.getConnectedService().getName().substring(0,2));
        }
        else{
            avatarContainer.setVisibility(View.GONE);
        }

        /*Messages and attachments are supposed to be added in container here*/
        ViewGroup cellContainer = (ViewGroup) convertView.findViewById(R.id.atlas_view_messages_cell_container);

        if(cellContainer.getChildCount()!=0)
            cellContainer.removeAllViews();

        cellContainer.addView(generateTextCell(msg,cellContainer));

        /*Adding attachments if present*/
        List<Attachment> attachments=msg.getAttachments();

        if(attachments!=null) {
            for (Attachment attachment : attachments) {

                switch (attachment.getMimeType()) {
                    case MIME_TYPE_TEXT:
                        cellContainer.addView(generateTextCell(attachment.getTextContent(),attachments.size(),msg.getSender().equals(client.getAuthContact()),cellContainer));
                    break;
                    case MIME_TYPE_IMAGE_PNG:
                        //image processing
                        cellContainer.addView(generateTextCell(attachment.getTextContent(),attachments.size(),msg.getSender().equals(client.getAuthContact()),cellContainer));
                        break;
                    case MIME_TYPE_IMAGE_JPEG:
                        //image processing
                        cellContainer.addView(generateTextCell(attachment.getTextContent(),attachments.size(),msg.getSender().equals(client.getAuthContact()),cellContainer));
                        break;
                    case MIME_TYPE_IMAGE_GIF:
                        //image processing
                        cellContainer.addView(generateTextCell(attachment.getTextContent(),attachments.size(),msg.getSender().equals(client.getAuthContact()),cellContainer));
                    break;
                    case MIME_TYPE_UNSUPPORTED:
                        cellContainer.addView(generateTextCell(attachment.getUri().toString(),attachments.size(),msg.getSender().equals(client.getAuthContact()),cellContainer));
                    break;
                    default:
                        cellContainer.addView(generateTextCell(attachment.getUri().toString(),attachments.size(),msg.getSender().equals(client.getAuthContact()),cellContainer));
                    break;
                }

            }
        }

        /*Conterpart message container shifted left with spacer*/
        View spacerRight = convertView.findViewById(R.id.atlas_view_messages_convert_spacer_right);
        if(msg.getSender().equals(client.getConnectedService())) {
            spacerRight.setVisibility(View.VISIBLE);
        }
        else{
            spacerRight.setVisibility(View.GONE);
        }

        /*Messages from out part shows delivery status (pending,sent or read)*/
        TextView receiptView = (TextView) convertView.findViewById(R.id.atlas_view_messages_convert_delivery_receipt);
        if(msg.getSender().equals(client.getAuthContact())){
            receiptView.setVisibility(View.VISIBLE);
            if(msg.isRead())
                receiptView.setText("Read");
            else if(msg.isSent())
                receiptView.setText("Sent");
            else
                receiptView.setText("Pending");
        }
        else
            receiptView.setVisibility(View.GONE);

        /*bottomSpacer also always visible*/
        //View spacerBottom = convertView.findViewById(R.id.atlas_view_messages_convert_spacer_bottom);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataGroupServices.getItemCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataGroupServices.getGroupData(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return dataGroupServices.getGroupCount();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.atlas_view_messages_group_convert,null);
        }

        DataGroup group=(DataGroup) getGroup(groupPosition);

        TextView dayField=(TextView) convertView.findViewById(R.id.atlas_view_messages_convert_timebar_day);
        TextView timeField=(TextView) convertView.findViewById(R.id.atlas_view_messages_convert_timebar_time);

        String strDay=formatTimeDay(group.getEndDate());
        String strTime=timeFormat.format(group.getStartDate().getTime());

        dayField.setText(strDay);
        timeField.setText(strTime);

        return convertView;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    /** Today, Yesterday, Weekday or Weekday + date */
    public String formatTimeDay(Date sentAt) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long todayMidnight = cal.getTimeInMillis();
        long yesterMidnight = todayMidnight - TIME_HOURS_24;
        long weekAgoMidnight = todayMidnight - TIME_HOURS_24 * 7;

        String timeBarDayText = null;
        if (sentAt.getTime() > todayMidnight) {
            timeBarDayText = "Today";
        } else if (sentAt.getTime() > yesterMidnight) {
            timeBarDayText = "Yesterday";
        } else if (sentAt.getTime() > weekAgoMidnight) {
            cal.setTime(sentAt);
            timeBarDayText = TIME_WEEKDAYS_NAMES[cal.get(Calendar.DAY_OF_WEEK) - 1];
        } else {
            timeBarDayText = sdfDayOfWeek.format(sentAt);
        }
        return timeBarDayText;
    }

    private View generateTextCell(String text,int numAttachments,boolean msgFromContact,ViewGroup cellContainer){
        View cellText = layoutInflater.inflate(R.layout.atlas_view_messages_cell_text, cellContainer, false);

        /*When text is empty there MUST be attachments. Let's point on them.*/
        if(text.isEmpty()){
            text="See "+numAttachments+" attachments:";
        }

        TextView textContact = (TextView) cellText.findViewById(R.id.atlas_view_messages_convert_text);
        TextView textService = (TextView) cellText.findViewById(R.id.atlas_view_messages_convert_text_counterparty);

        if (msgFromContact) {
            textContact.setVisibility(View.VISIBLE);
            textContact.setAutoLinkMask(Linkify.ALL);
            textContact.setLinksClickable(true);
            textContact.setText(text);

            textService.setVisibility(View.GONE);

            textContact.setBackgroundResource(R.drawable.atlas_shape_rounded16_blue);

            /*if (CLUSTERED_BUBBLES) {
                if (cell.clusterHeadItemId == cell.clusterItemId && !cell.clusterTail) {
                    textContact.setBackgroundResource(R.drawable.atlas_shape_rounded16_blue_no_bottom_right);
                } else if (cell.clusterTail && cell.clusterHeadItemId != cell.clusterItemId) {
                    textContact.setBackgroundResource(R.drawable.atlas_shape_rounded16_blue_no_top_right);
                } else if (cell.clusterHeadItemId != cell.clusterItemId && !cell.clusterTail) {
                    textContact.setBackgroundResource(R.drawable.atlas_shape_rounded16_blue_no_right);
                }
            }*/

            ((GradientDrawable)textContact.getBackground()).setColor(styleSettings.getMyBubbleColor());
            textContact.setTextColor(styleSettings.getMyTextColor());
            //textContact.setTextSize(TypedValue.COMPLEX_UNIT_DIP, myTextSize);
            textContact.setTypeface(styleSettings.getMyTextTypeface(), styleSettings.getMyTextStyle());
        }
        else {
            textService.setVisibility(View.VISIBLE);
            textService.setAutoLinkMask(Linkify.ALL);
            textService.setLinksClickable(true);
            textService.setText(text);

            textContact.setVisibility(View.GONE);

            textService.setBackgroundResource(R.drawable.atlas_shape_rounded16_gray);

            /*if (CLUSTERED_BUBBLES) {
                if (cell.clusterHeadItemId == cell.clusterItemId && !cell.clusterTail) {
                    textService.setBackgroundResource(R.drawable.atlas_shape_rounded16_gray_no_bottom_left);
                } else if (cell.clusterTail && cell.clusterHeadItemId != cell.clusterItemId) {
                    textService.setBackgroundResource(R.drawable.atlas_shape_rounded16_gray_no_top_left);
                } else if (cell.clusterHeadItemId != cell.clusterItemId && !cell.clusterTail) {
                    textService.setBackgroundResource(R.drawable.atlas_shape_rounded16_gray_no_left);
                }
            }*/

            ((GradientDrawable)textService.getBackground()).setColor(styleSettings.getOtherBubbleColor());
            textService.setTextColor(styleSettings.getOtherTextColor());
            //textService.setTextSize(TypedValue.COMPLEX_UNIT_DIP, otherTextSize);
            textService.setTypeface(styleSettings.getOtherTextTypeface(), styleSettings.getOtherTextStyle());
        }

        return cellText;
    }

    private View generateTextCell(Message msg,ViewGroup cellContainer){
        if(msg.getAttachments()!=null)
            return generateTextCell(msg.getBody(),msg.getAttachments().size(),msg.getSender().equals(client.getAuthContact()),cellContainer);
        else
            return generateTextCell(msg.getBody(),0,msg.getSender().equals(client.getAuthContact()),cellContainer);
    }
}
