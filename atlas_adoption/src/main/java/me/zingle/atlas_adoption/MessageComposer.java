package me.zingle.atlas_adoption;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.utils.Client;

/**
 * Created by SLAVA 09 2015.
 */
public class MessageComposer extends FrameLayout {
    private EditText messageText;
    private View btnSend;
    private View btnUpload;

    Message createdMessage=new Message();

    private Listener listener;
    private Client layerClient;

    private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

    // styles
    private int textColor;
    private float textSize;
    private Typeface typeFace;
    private int textStyle;

    //
    public MessageComposer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseStyle(context, attrs, defStyle);
    }

    public MessageComposer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageComposer(Context context) {
        super(context);
    }

    public void parseStyle(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AtlasMessageComposer, R.attr.AtlasMessageComposer, defStyle);
        this.textColor = ta.getColor(R.styleable.AtlasMessageComposer_composerTextColor, context.getResources().getColor(R.color.atlas_text_black));
        //this.textSize  = ta.getDimension(R.styleable.AtlasMessageComposer_composerTextSize, context.getResources().getDimension(R.dimen.atlas_text_size_general));
        this.textStyle = ta.getInt(R.styleable.AtlasMessageComposer_composerTextStyle, Typeface.NORMAL);
        String typeFaceName = ta.getString(R.styleable.AtlasMessageComposer_composerTextTypeface);
        this.typeFace  = typeFaceName != null ? Typeface.create(typeFaceName, textStyle) : null;
        ta.recycle();
    }

    /**
     * Initialization is required to engage MessageComposer with LayerClient and Conversation
     * to send messages.
     * <p>
     * If Conversation is not defined, "Send" action will not be able to send messages
     *
     * @param client - must be not null
     */
    public void init(final Client client) {
        if (client == null) throw new IllegalArgumentException("LayerClient cannot be null");
        if (messageText != null) throw new IllegalStateException("AtlasMessageComposer is already initialized!");

        this.layerClient = client;

        LayoutInflater.from(getContext()).inflate(R.layout.atlas_message_composer, this);

        btnUpload = findViewById(R.id.atlas_message_composer_upload);
        btnUpload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final PopupWindow popupWindow = new PopupWindow(v.getContext());
                popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                LinearLayout menu = (LinearLayout) inflater.inflate(R.layout.atlas_view_message_composer_menu, null);
                popupWindow.setContentView(menu);

                for (MenuItem item : menuItems) {
                    View itemConvert = inflater.inflate(R.layout.atlas_view_message_composer_menu_convert, menu, false);
                    TextView titleText = ((TextView) itemConvert.findViewById(R.id.altas_view_message_composer_convert_text));
                    titleText.setText(item.title);
                    itemConvert.setTag(item);
                    itemConvert.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            MenuItem item = (MenuItem) v.getTag();
                            if (item.clickListener != null) {
                                item.clickListener.onClick(v);
                            }
                        }
                    });
                    menu.addView(itemConvert);
                }
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.setOutsideTouchable(true);
                int[] viewXYWindow = new int[2];
                v.getLocationInWindow(viewXYWindow);

                menu.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int menuHeight = menu.getMeasuredHeight();
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, viewXYWindow[0], viewXYWindow[1] - menuHeight);
            }
        });

        messageText = (EditText) findViewById(R.id.atlas_message_composer_text);
        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSend = findViewById(R.id.atlas_message_composer_send);
        btnSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                String text = messageText.getText().toString();

                if (text.trim().length() > 0 || createdMessage.getAttachments().size()>0) {

                    createdMessage.setBody(text);
                    createdMessage.setSender(client.getAuthContact());
                    createdMessage.setRecipient(client.getConnectedService());
                    createdMessage.setChannelTypeId(client.getChannelTypeId());
                    createdMessage.setCreatedAt(new Date());

                    if (listener != null) {
                        boolean proceed = listener.beforeSend(createdMessage);
                        if (!proceed) return;
                    }
                    messageText.setText("");
                    createdMessage=new Message();
                }
            }
        });

        applyStyle();
    }

    private void applyStyle() {
        //messageText.setTextSize(textSize);
        messageText.setTypeface(typeFace, textStyle);
        messageText.setTextColor(textColor);
    }

    public void registerMenuItem(String title, OnClickListener clickListener) {
        if (title == null) throw new NullPointerException("Item title must not be null");
        MenuItem item = new MenuItem();
        item.title = title;
        item.clickListener = clickListener;
        menuItems.add(item);
        btnUpload.setVisibility(View.VISIBLE);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        boolean beforeSend(Message message);
    }

    private static class MenuItem {
        String title;
        OnClickListener clickListener;
    }

}
