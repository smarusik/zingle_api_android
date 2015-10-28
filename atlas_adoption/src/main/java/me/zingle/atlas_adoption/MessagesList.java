package me.zingle.atlas_adoption;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import java.text.DateFormat;

import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.model_view.MessageListStyleSettings;
import me.zingle.atlas_adoption.model_view.ZingleListAdapter;
import me.zingle.atlas_adoption.utils.Client;

/**
 * Created by SLAVA 09 2015.
 */
public class MessagesList extends FrameLayout{

    //properties from Atlas
    private final DateFormat timeFormat;
    private ExpandableListView messagesList;
    private Client client=Client.getItem();
    ZingleListAdapter adapter;



    //own properties

    private MessageListStyleSettings styleSettings;


    public void parseStyle(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AtlasMessageList, R.attr.AtlasMessageList, defStyle);
        styleSettings.setMyTextColor(ta.getColor(R.styleable.AtlasMessageList_myTextColor, context.getResources().getColor(R.color.atlas_text_black)));
        styleSettings.setMyTextStyle(ta.getInt(R.styleable.AtlasMessageList_myTextStyle, Typeface.NORMAL));
        String myTextTypefaceName = ta.getString(R.styleable.AtlasMessageList_myTextTypeface);
        styleSettings.setMyTextTypeface(myTextTypefaceName != null ? Typeface.create(myTextTypefaceName, styleSettings.getMyTextStyle()) : null);
        //this.myTextSize = ta.getDimension(R.styleable.AtlasMessageList_myTextSize, context.getResources().getDimension(R.dimen.atlas_text_size_general));

        styleSettings.setOtherTextColor(ta.getColor(R.styleable.AtlasMessageList_theirTextColor, context.getResources().getColor(R.color.atlas_text_black)));
        styleSettings.setOtherTextStyle(ta.getInt(R.styleable.AtlasMessageList_theirTextStyle, Typeface.NORMAL));
        String otherTextTypefaceName = ta.getString(R.styleable.AtlasMessageList_theirTextTypeface);
        styleSettings.setOtherTextTypeface(otherTextTypefaceName != null ? Typeface.create(otherTextTypefaceName, styleSettings.getOtherTextStyle()) : null);
        //this.otherTextSize = ta.getDimension(R.styleable.AtlasMessageList_theirTextSize, context.getResources().getDimension(R.dimen.atlas_text_size_general));

        styleSettings.setMyBubbleColor(ta.getColor(R.styleable.AtlasMessageList_myBubbleColor, context.getResources().getColor(R.color.atlas_bubble_blue)));
        styleSettings.setOtherBubbleColor(ta.getColor(R.styleable.AtlasMessageList_theirBubbleColor, context.getResources().getColor(R.color.atlas_background_gray)));

        styleSettings.setDateTextColor(ta.getColor(R.styleable.AtlasMessageList_dateTextColor, context.getResources().getColor(R.color.atlas_text_gray)));
        /*this.avatarTextColor = ta.getColor(R.styleable.AtlasMessageList_avatarTextColor, context.getResources().getColor(R.color.atlas_text_black));
        this.avatarBackgroundColor = ta.getColor(R.styleable.AtlasMessageList_avatarBackgroundColor, context.getResources().getColor(R.color.atlas_background_gray));*/
        ta.recycle();
    }

    public MessagesList(Context context,AttributeSet attrs) {
        super(context,attrs,0);
        styleSettings=new MessageListStyleSettings();
        parseStyle(context,attrs,0);
        this.timeFormat = android.text.format.DateFormat.getTimeFormat(context);
    }


    public void init(Activity activity){
        LayoutInflater.from(getContext()).inflate(R.layout.atlas_messages_list, this);
        messagesList = (ExpandableListView) findViewById(R.id.atlas_messages_list);

        DataServices dataServices=DataServices.getItem();
        dataServices.initConversation(this);

        adapter=new ZingleListAdapter(activity, client, styleSettings);
        messagesList.setAdapter(adapter);

        for(int i=0;i<adapter.getGroupCount();i++) messagesList.expandGroup(i);

        showLastMessage();
    }

    public void showLastMessage(){
        int numGroups=adapter.getGroupCount();
        if(numGroups>0) {
            messagesList.expandGroup(numGroups - 1);
            messagesList.setSelectedChild(numGroups - 1, adapter.getChildrenCount(numGroups - 1), true);
        }
    }

    public void reloadMessagesList(){
       adapter.notifyDataSetChanged();
    }

    public boolean isOnScreen(){
        return client.isListVisible();
    }
}
