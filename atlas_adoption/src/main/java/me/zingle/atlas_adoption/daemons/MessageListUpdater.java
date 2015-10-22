package me.zingle.atlas_adoption.daemons;

import me.zingle.atlas_adoption.model_view.DataServices;

/**
 * Created by SLAVA 10 2015.
 */
public class MessageListUpdater implements Runnable{

    private final DataServices dataServices=DataServices.getItem();
    private boolean showLastMessage;

    public MessageListUpdater() {
        this(false);
    }

    public MessageListUpdater(boolean showLastMessage) {
        this.showLastMessage = showLastMessage;
    }

    @Override
    public void run() {
        dataServices.updateMessagesList();

        if(showLastMessage)
            dataServices.showEndOfMessagesList();
    }
}
