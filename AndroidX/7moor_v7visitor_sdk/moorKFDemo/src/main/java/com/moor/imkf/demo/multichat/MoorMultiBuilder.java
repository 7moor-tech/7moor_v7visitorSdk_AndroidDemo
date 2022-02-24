package com.moor.imkf.demo.multichat;

import androidx.annotation.NonNull;

import com.moor.imkf.demo.multichat.multirow.MoorCSRReceviedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFastBtnReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFileReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFileSendViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFlowListMultiSelectViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFlowListSingleHorizontalViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFlowListSingleVerticalViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFlowListTextViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorFlowListTwoViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorImageReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorImageSendViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorLogisticsReceviedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorOrderListSendViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorRobotUseFulReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorSystemReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorTextReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorTextSendViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorVoiceSendViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorXbotQuickMenuReceivedViewBinder;
import com.moor.imkf.demo.multichat.multirow.MoorXbotTabQuestionViewBinder;
import com.moor.imkf.demo.multitype.MoorClassLinker;
import com.moor.imkf.demo.multitype.MoorItemViewBinder;
import com.moor.imkf.demo.multitype.MoorMultiTypeAdapter;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 会话多类型消息构造器
 *     @version: 1.0
 * </pre>
 */
public class MoorMultiBuilder {
    private MoorOptions options;

    private MoorMultiBuilder() {
    }

    public static MoorMultiBuilder getInstance() {
        return MoorMultiBuilder.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorMultiBuilder sInstance = new MoorMultiBuilder();
    }

    public void setMoorOptions(MoorOptions options) {
        this.options = options;
    }

    public void build(MoorMultiTypeAdapter adapter) {
        adapter.register(MoorMsgBean.class).to(
                new MoorTextReceivedViewBinder(options)
                , new MoorTextSendViewBinder(options)
                , new MoorImageReceivedViewBinder()
                , new MoorImageSendViewBinder()
                , new MoorVoiceSendViewBinder(options)
                , new MoorFileReceivedViewBinder(options)
                , new MoorFileSendViewBinder(options)
                , new MoorXbotTabQuestionViewBinder(options)
                , new MoorSystemReceivedViewBinder(options)
                , new MoorCSRReceviedViewBinder(options)
                , new MoorFlowListTextViewBinder(options)
                , new MoorFlowListSingleHorizontalViewBinder(options)
                , new MoorFlowListSingleVerticalViewBinder(options)
                , new MoorFlowListTwoViewBinder(options)
                , new MoorFlowListMultiSelectViewBinder(options)
                , new MoorLogisticsReceviedViewBinder(options)
                , new MoorRobotUseFulReceivedViewBinder(options)
                , new MoorOrderListSendViewBinder(options)
                , new MoorFastBtnReceivedViewBinder(options)
                , new MoorXbotQuickMenuReceivedViewBinder(options)
        ).withClassLinker(new MoorClassLinker<MoorMsgBean>() {
            @NonNull
            @Override
            public Class<? extends MoorItemViewBinder<MoorMsgBean, ?>> index(int position, @NonNull MoorMsgBean message) {
                if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_TEXT)) {
                    if (message.getFrom().equals(MoorChatMsgType.MSG_TYPE_RECEIVED)) {
                        return MoorTextReceivedViewBinder.class;
                    } else {
                        return MoorTextSendViewBinder.class;
                    }
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_IMAGE)) {
                    if (message.getFrom().equals(MoorChatMsgType.MSG_TYPE_RECEIVED)) {
                        return MoorImageReceivedViewBinder.class;
                    } else {
                        return MoorImageSendViewBinder.class;
                    }
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_VOICE)) {
                    if (message.getFrom().equals(MoorChatMsgType.MSG_TYPE_RECEIVED)) {
                        return MoorTextReceivedViewBinder.class;
                    } else {
                        return MoorVoiceSendViewBinder.class;
                    }
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_FILE)) {
                    if (message.getFrom().equals(MoorChatMsgType.MSG_TYPE_RECEIVED)) {
                        return MoorFileReceivedViewBinder.class;
                    } else {
                        return MoorFileSendViewBinder.class;
                    }
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_TAB_QUESTION)) {
                    return MoorXbotTabQuestionViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_SYSTEM)
                        || message.getContentType().equals(MoorChatMsgType.MSG_TYPE_CLAIM)
                        || message.getContentType().equals(MoorChatMsgType.MSG_TYPE_ROBOT_INIT)
                        || message.getContentType().equals(MoorChatMsgType.MSG_TYPE_REDIRECT)) {
                    return MoorSystemReceivedViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_CSR)) {
                    return MoorCSRReceviedViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_FLOW_LIST_TEXT)) {
                    return MoorFlowListTextViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_FLOW_LIST_SINGLE_ONE_HORIZONTAL)) {
                    return MoorFlowListSingleHorizontalViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_FLOW_LIST_SINGLE_ONE_VERTICAL)) {
                    return MoorFlowListSingleVerticalViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_FLOW_LIST_TWO)) {
                    return MoorFlowListTwoViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_FLOW_LIST_MULTI_SELECT)) {
                    return MoorFlowListMultiSelectViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_ROBOT_USEFUL)) {
                    return MoorRobotUseFulReceivedViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_LOGISTICS)) {
                    return MoorLogisticsReceviedViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_ORDER_CARD_INFO)) {
                    return MoorOrderListSendViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_LIST_FAST_BTN)) {
                    return MoorFastBtnReceivedViewBinder.class;
                } else if (message.getContentType().equals(MoorChatMsgType.MSG_TYPE_LIST_XBOT_QUICKMENU)) {
                    return MoorXbotQuickMenuReceivedViewBinder.class;
                }
                {
                    if (message.getFrom().equals(MoorChatMsgType.MSG_TYPE_RECEIVED)) {
                        return MoorTextReceivedViewBinder.class;
                    } else {
                        return MoorTextSendViewBinder.class;
                    }
                }


            }
        });
    }


}
