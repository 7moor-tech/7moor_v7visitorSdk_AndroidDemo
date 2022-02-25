package com.moor.imkf.demo.fragment;

import static com.moor.imkf.demo.multitype.MoorMultiTypeAsserts.assertAllRegistered;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorChatAssociateAdapter;
import com.moor.imkf.demo.adapter.MoorChatBottomLabelsAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.bean.MoorEvaluationBean;
import com.moor.imkf.demo.bean.MoorGetListMsgBean;
import com.moor.imkf.demo.bean.MoorIconUrlBean;
import com.moor.imkf.demo.bean.MoorTransferAgentEvent;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.constans.MoorEnumControlViewState;
import com.moor.imkf.demo.emotion.MoorEmotionPagerView;
import com.moor.imkf.demo.helper.MoorPanelDataHelper;
import com.moor.imkf.demo.helper.MoorTakePicturesHelper;
import com.moor.imkf.demo.listener.IMoorBinderClickListener;
import com.moor.imkf.demo.listener.IMoorEmojiClickListener;
import com.moor.imkf.demo.listener.IMoorOnClickEvaListener;
import com.moor.imkf.demo.listener.IMoorPanelOnClickListener;
import com.moor.imkf.demo.multichat.MoorMultiBuilder;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multitype.MoorMultiTypeAdapter;
import com.moor.imkf.demo.panel.MoorKeyboardUtil;
import com.moor.imkf.demo.panel.MoorSwitchConflictUtil;
import com.moor.imkf.demo.panel.MoorSwitchFSPanelLinearLayout;
import com.moor.imkf.demo.panel.MoorSwitchRootLinearLayout;
import com.moor.imkf.demo.preloader.MoorPreLoader;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataListener;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorEmojiBitmapUtil;
import com.moor.imkf.demo.utils.MoorFileFormatUtils;
import com.moor.imkf.demo.utils.MoorFileUtils;
import com.moor.imkf.demo.utils.MoorMediaPlayTools;
import com.moor.imkf.demo.utils.MoorMimeTypesTools;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.utils.permission.MoorPermissionConstants;
import com.moor.imkf.demo.utils.permission.MoorPermissionUtil;
import com.moor.imkf.demo.utils.permission.callback.OnRequestCallback;
import com.moor.imkf.demo.utils.statusbar.MoorStatusBarUtil;
import com.moor.imkf.demo.view.MoorBottomLogisticsProgressDialog;
import com.moor.imkf.demo.view.MoorBottomTabQuestionDialog;
import com.moor.imkf.demo.view.MoorCommonBottomDialog;
import com.moor.imkf.demo.view.MoorEvaluationDialog;
import com.moor.imkf.demo.view.MoorLinearLayoutManager;
import com.moor.imkf.demo.view.MoorPanelView;
import com.moor.imkf.demo.view.MoorRoundImageView;
import com.moor.imkf.demo.view.MoorSpaceItemDecoration;
import com.moor.imkf.demo.view.audiobutton.MoorAudioRecorderButton;
import com.moor.imkf.demo.view.loading.MoorLoadingDialog;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.lib.constants.MoorPathConstants;
import com.moor.imkf.lib.http.builder.MoorPostFormBuilder;
import com.moor.imkf.lib.http.callback.MoorBaseCallBack;
import com.moor.imkf.lib.http.donwload.IMoorOnDownloadListener;
import com.moor.imkf.lib.http.donwload.MoorDownLoadUtils;
import com.moor.imkf.lib.jobqueue.base.JobManager;
import com.moor.imkf.lib.utils.MoorAntiShakeUtils;
import com.moor.imkf.lib.utils.MoorLogUtils;
import com.moor.imkf.lib.utils.sharedpreferences.MoorSPUtils;
import com.moor.imkf.moorhttp.MoorHttpParams;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorhttp.httpparse.MoorHttpParse;
import com.moor.imkf.moorsdk.bean.MoorBottomBtnBean;
import com.moor.imkf.moorsdk.bean.MoorCheckCsrStatusBean;
import com.moor.imkf.moorsdk.bean.MoorEmotion;
import com.moor.imkf.moorsdk.bean.MoorFastBtnBean;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorInfoBean;
import com.moor.imkf.moorsdk.bean.MoorInputSuggestBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorNetBaseBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.bean.MoorOrderCardBean;
import com.moor.imkf.moorsdk.bean.MoorOrderInfoBean;
import com.moor.imkf.moorsdk.bean.MoorPanelBean;
import com.moor.imkf.moorsdk.bean.MoorQuickMenuBean;
import com.moor.imkf.moorsdk.bean.MoorServerTimeBean;
import com.moor.imkf.moorsdk.bean.MoorUploadBean;
import com.moor.imkf.moorsdk.bean.MoorXbotQuickEvent;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;
import com.moor.imkf.moorsdk.constants.MoorConstants;
import com.moor.imkf.moorsdk.constants.MoorEvaluationContans;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
import com.moor.imkf.moorsdk.db.MoorMsgDao;
import com.moor.imkf.moorsdk.db.MoorMsgHelper;
import com.moor.imkf.moorsdk.db.MoorOptionsDao;
import com.moor.imkf.moorsdk.events.MoorBlackListEvent;
import com.moor.imkf.moorsdk.events.MoorBottomListEvent;
import com.moor.imkf.moorsdk.events.MoorClaimEvent;
import com.moor.imkf.moorsdk.events.MoorClearStatusEvent;
import com.moor.imkf.moorsdk.events.MoorCloseSessionEvent;
import com.moor.imkf.moorsdk.events.MoorLeaveEvent;
import com.moor.imkf.moorsdk.events.MoorManualEvent;
import com.moor.imkf.moorsdk.events.MoorNewChatEvent;
import com.moor.imkf.moorsdk.events.MoorQueueEvent;
import com.moor.imkf.moorsdk.events.MoorReadStatusEvent;
import com.moor.imkf.moorsdk.events.MoorRobotEvent;
import com.moor.imkf.moorsdk.events.MoorShowUnblockButtonEvent;
import com.moor.imkf.moorsdk.events.MoorSocketNewMsg;
import com.moor.imkf.moorsdk.events.MoorWithdrawMessage;
import com.moor.imkf.moorsdk.listener.IMoorMsgSendObservable;
import com.moor.imkf.moorsdk.listener.IMoorMsgobserver;
import com.moor.imkf.moorsdk.listener.IMoorUploadCallBackListener;
import com.moor.imkf.moorsdk.manager.MoorActivityHolder;
import com.moor.imkf.moorsdk.manager.MoorConfiguration;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.moorjob.MoorMessageJob;
import com.moor.imkf.moorsdk.upload.MoorUploadHelper;
import com.moor.imkf.moorsdk.utils.MoorEventBusUtil;
import com.moor.imkf.lib.utils.MoorSdkVersionUtil;
import com.moor.imkf.moorsdk.utils.MoorUtils;
import com.moor.imkf.moorsdk.utils.toast.MoorToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/5/17
 *     @desc   : 会话页面
 *     @version: 1.0
 * </pre>
 */
public class MoorChatFragment extends Fragment implements IMoorPanelOnClickListener, View.OnClickListener {
    private Activity mActivity;
    private MoorConfiguration moorConfiguration;
    private View moorChatView, moorChatTipView;
    private SwipeRefreshLayout moorSwipe;
    private RecyclerView mRvChat;
    private MoorRoundImageView mChatEmojiNormal;
    private MoorRoundImageView mChatSetModeVoice;
    private MoorRoundImageView mChatSetModeKeyboard;
    private MoorRoundImageView mBtChooseMore;
    private MoorEmotionPagerView mMoorEmotionPagerView;
    private EditText mEtChatInput;
    private MoorRoundImageView mBtChatSend;
    private MoorLinearLayoutManager layoutManager;
    private MoorMultiTypeAdapter adapter;
    private MoorSwitchFSPanelLinearLayout mPanelRoot;
    private MoorAudioRecorderButton audioRecordButton;
    private View mPanelEmotion;
    private View mPanelMore;
    private TextView mTvChatLogout;
    private RelativeLayout rlTitle, rlTopTip;
    private TextView tvChatConvert, tvMoorTip, tvQueueNum, tv_queue_num_right, tv_queue_num_left, tv_chat_title;
    private TextView mTvSendEmoji;
    private MoorShadowLayout slSendEmoji, slCancelQueue;
    private ImageView mIvDeleteEmoji, ivTipClose, iv_tip_img, ivCancelQueue;
    private MoorPanelView mChatPanelView;
    private MoorSwitchRootLinearLayout rootLinearLayout;
    private RecyclerView rvBottomtag;
    private LinearLayout llBottomLayout;
    private LinearLayout llQueueLayout;
    private LinearLayout llLayoutEmoji;
    private RecyclerView rvEditAssociate;
    private List<MoorMsgBean> msgList = new ArrayList<>();
    private MoorOptions options;
    private int msgPage = 1;
    private ChatHandler chatHandler;
    /**
     * 预加载标识id
     */
    private int preLoaderId;
    private MoorInfoBean infoBean;
    /**
     * 评价信息数据
     */
    private MoorEvaluationBean moorEvaluationBean;
    private boolean keyBoardIsShowing;
    private MoorLoadingDialog loadingDialog;
    /**
     * 网络失去 或ws断开弹窗
     */
    private MoorCommonBottomDialog wsErrorDialog;
    private MoorSpaceItemDecoration decor;
    /**
     * 页面是否加载,用于表情点击判断
     */
    private boolean hadInit;
    private LinearLayout llChatLogout;
    private ImageView ivChatLogout;
    private ImageView ivChatConvert;
    private LinearLayout llChatConvert;
    private TextView tvChatClose;
    private LinearLayout llChatClose;
    private ImageView ivChatClose;
    private MoorShadowLayout slChatInput;

    /**
     * 消息列表中横向滑动的快速按钮数据List
     */
    public ArrayList<MoorFastBtnBean> fastBtnBeans;
    private boolean isFront = false;//记录当前页面是否在前台
    //解封词
    private String unblockContent;
    /**
     * adapter中的点击和长按等事件
     */
    private final IMoorBinderClickListener adapterClick = new IMoorBinderClickListener() {

        @Override
        public void onClick(View v, final MoorMsgBean item, MoorEnumChatItemClickType type) {
            if (type == MoorEnumChatItemClickType.TYPE_RESEND_MSG) {
                Object[] obj = (Object[]) type.getObj();
                //消息重发
                reSendMsg(item);
            } else if (type == MoorEnumChatItemClickType.TYPE_FILE_CLICK) {
                //点击附件
                Object[] obj = (Object[]) type.getObj();
                dealFileClickEvent(item, obj);
            } else if (type == MoorEnumChatItemClickType.TYPE_CSR_CLICK) {
                //满意度评价点击
                openEvaluationDialog(item.getMessageId(), item, "");
            } else if (type == MoorEnumChatItemClickType.TYPE_FLOW_LIST) {
                infoBean = MoorInfoDao.getInstance().queryInfo();
//                if (!item.isHistory() && infoBean.getSessionId().equals(item.getSessionId())) {
                //flowList列表点击
                Object[] obj = (Object[]) type.getObj();
                MoorFlowListBean flowListBean = (MoorFlowListBean) obj[0];
                String recogType = flowListBean.getRecogType();
                if ("4".equals(recogType)) {
                    // TODO:   可以自己定制点击事件
                    MoorToastUtils.showShort(flowListBean.getText());
                } else {
                    sendTextMsg(flowListBean.getText());
                }
//                }
            } else if (type == MoorEnumChatItemClickType.TYPE_FLOW_LIST_TEXT) {
                //flowList 文字点击
                Object[] obj = (Object[]) type.getObj();
                MoorFlowListBean flowListBean = (MoorFlowListBean) obj[0];
                String recogType = flowListBean.getRecogType();
                if ("4".equals(recogType)) {
                    // TODO:   可以自己定制点击事件
                    MoorToastUtils.showShort(flowListBean.getText());
                } else {
                    sendTextMsg(flowListBean.getText());
                }
            } else if (type == MoorEnumChatItemClickType.TYPE_FLOW_LIST_MULTI) {
                infoBean = MoorInfoDao.getInstance().queryInfo();
                if (infoBean.getSessionId().equals(item.getSessionId())) {
                    //flowList多选点击
                    Object[] obj = (Object[]) type.getObj();
                    List<MoorFlowListBean> flowListBean = (List<MoorFlowListBean>) obj[0];
                    StringBuilder builder = new StringBuilder();
                    for (MoorFlowListBean bean : flowListBean) {
                        builder.append("【" + bean.getText() + "】、");
                    }
                    String s = builder.toString();
                    String substring = s.substring(0, s.length() - 1);
                    sendTextMsg(substring);
                    //
                    //更新列表数据
                    int index = 0;
                    for (int i = msgList.size() - 1; i >= 0; i--) {
                        if (msgList.get(i).getMessageId().equals(item.getMessageId())) {
                            msgList.get(i).setHasSendMulti(true);
                            index = i;
                            break;
                        }
                    }
                    //刷新页面
                    if (adapter != null) {
                        adapter.notifyItemChanged(index);
                    }
                }
            } else if (type == MoorEnumChatItemClickType.TYPE_USEFUL) {
                //点赞、点踩
                Object[] obj = (Object[]) type.getObj();
                final boolean isUseFul = (boolean) obj[0];
                dealRobotFeedBack(item, isUseFul);
            } else if (type == MoorEnumChatItemClickType.TYPE_XBOT_TABQUESTION_ITEM) {
                Object[] obj = (Object[]) type.getObj();
                String qa = (String) obj[0];
                sendTextMsg(qa);
            } else if (type == MoorEnumChatItemClickType.TYPE_XBOT_TABQUESTION_MORE) {
                Object[] obj = (Object[]) type.getObj();
                String tittle = (String) obj[0];
                ArrayList<String> message = (ArrayList<String>) obj[1];
                handleTabQuestionMoreClick(tittle, message);
            } else if (type == MoorEnumChatItemClickType.TYPE_LOGISTICS_MORE) {
                Object[] obj = (Object[]) type.getObj();
                ArrayList<MoorOrderInfoBean> list = (ArrayList<MoorOrderInfoBean>) obj[0];
                String tittle = (String) obj[1];
                String num = (String) obj[2];
                String dialogTitle = (String) obj[3];
                showMoreLogistics(list, tittle, num, dialogTitle);
            } else if (type == MoorEnumChatItemClickType.TYPE_ORDER_CARD_CLICK) {
                Object[] obj = (Object[]) type.getObj();
                clickOrderCard(obj);
            } else if (type == MoorEnumChatItemClickType.TYPE_FAST_BTN_LIST) {
                //初始化配置的快捷按钮
                Object[] obj = (Object[]) type.getObj();
                MoorFastBtnBean fastBtnBean = (MoorFastBtnBean) obj[0];
                sendTextMsg(fastBtnBean.getClickText());
            } else if (type == MoorEnumChatItemClickType.TYPE_XBOT_QUICKMENU_CLICK) {
                //xobt quickmenu 按钮点击
                Object[] obj = (Object[]) type.getObj();
                MoorQuickMenuBean quickMenuBean = (MoorQuickMenuBean) obj[0];
                if (quickMenuBean != null) {
                    if (quickMenuBean.getButton_type() == 2) {
                        if (!TextUtils.isEmpty(quickMenuBean.getContent())) {
                            //跳转浏览器
                            MoorActivityHolder.getCurrentActivity().startActivity(MoorUtils.getStartWebActionIntent(quickMenuBean.getContent()));
                        }
                    } else {
                        if (!TextUtils.isEmpty(quickMenuBean.getContent())) {
                            sendTextMsg(quickMenuBean.getContent());
                        }
                    }
                }
            }
        }

        @Override
        public void onLongClick(View v, MoorMsgBean item, Object o) {

        }
    };


    private final IMoorMsgobserver iMoorMsgobserver = new IMoorMsgobserver() {


        /**
         * 消息开始发送
         * @param msgBean 消息
         */
        @Override
        public void onMoorMsgSendStart(MoorMsgBean msgBean) {
            MoorLogUtils.d(msgBean.getMessageId());
        }

        /**
         * 发送成功
         *
         * @param oldMsg  本地消息
         * @param newMsg 发送消息后返回的消息体
         */
        @Override
        public void onMoorMsgSendComplete(final MoorMsgBean oldMsg, final MoorMsgBean newMsg) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateMsg(oldMsg, newMsg);
                }
            });

        }

        /**
         * 发送失败
         *
         * @param oldMsg   本地消息
         * @param errorMsg 报错信息
         */
        @Override
        public void onMoorMsgSendFail(final MoorMsgBean oldMsg, String errorMsg) {
            MoorLogUtils.e(errorMsg);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateMsg(oldMsg, null);
                    checkNetDialog();
                }
            });
        }
    };


    /**
     * 发送消息后，更新页面、数据
     *
     * @param oldMsg 本地数据
     * @param newMsg 接口返回的新数据
     */
    private void updateMsg(MoorMsgBean oldMsg, MoorMsgBean newMsg) {
        MoorMsgBean moorMsgBean = MoorMsgDao.getInstance().queryMsgById(oldMsg.getMessageId());
        if (moorMsgBean != null) {
            if (newMsg == null) {
                //发送失败
                moorMsgBean.setStatus(MoorChatMsgType.MSG_TYPE_STATUS_FAILURE);
            } else {
                //发送成功
                moorMsgBean.setStatus(MoorChatMsgType.MSG_TYPE_STATUS_SUCCESS)
                        .setContent(newMsg.getFilterMessage())
                        .setUserId(infoBean.getUserId())
                        .setCreateTimestamp(newMsg.getCreateTimestamp())
                        .setMessageId(newMsg.getMessageId());

            }

            //更新数据库
            MoorMsgDao.getInstance().updateMoorMsgBeanToDao(moorMsgBean);
            //更新列表数据
            int index = msgList.size() - 1;
            for (int i = msgList.size() - 1; i >= 0; i--) {
                MoorMsgBean msgBean = msgList.get(i);
                if (msgBean != null) {
                    if (msgBean.getMessageId().equals(oldMsg.getMessageId())) {
                        msgBean.setStatus(moorMsgBean.getStatus())
                                .setContent(moorMsgBean.getContent())
                                .setUserId(infoBean.getUserId())
                                .setCreateTimestamp(moorMsgBean.getCreateTimestamp())
                                .setMessageId(moorMsgBean.getMessageId());
                        index = i;
                        break;
                    }
                }
            }
            //刷新页面
            if (adapter != null) {
                adapter.notifyItemChanged(index);
            }
            scrollToBottom();
        }

    }

    /**
     * 内部静态Handler
     */
    private static class ChatHandler extends Handler {
        private final WeakReference<MoorChatFragment> mActivity;

        ChatHandler(MoorChatFragment fragment) {
            this.mActivity = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MoorChatFragment chatfragment = mActivity.get();
            chatfragment.handleMessage(msg);
        }
    }

    public static MoorChatFragment newInstance(int preLoaderId) {
        Bundle args = new Bundle();
        args.putInt(MoorDemoConstants.MOOR_PRE_LOADER_ID, preLoaderId);
        MoorChatFragment fragment = new MoorChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            getActivity().finish();
        }
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        moorChatView = View.inflate(mActivity, R.layout.moor_activity_chat, null);
        chatHandler = new ChatHandler(this);
        MoorManager.getInstance().setSDKInitFinish(true);
        loadingDialog = MoorLoadingDialog.create();
        initView();
        preLoaderId = getArguments().getInt(MoorDemoConstants.MOOR_PRE_LOADER_ID, 0);

        options = MoorOptionsDao.getInstance().queryOptions();

        //机器人状态下是否显示加号，默认true展示
        options.setRobotShowPanelButton(true);

        MoorManager.getInstance().setOptions(options);
        moorConfiguration = MoorManager.getInstance().getMoorConfiguration();


        infoBean = MoorInfoDao.getInstance().queryInfo();
        installOptions(options);

        if (moorConfiguration != null) {
            fastBtnBeans = moorConfiguration.fastBtnBeans;
        }

        attachPanel();
        configProperties();
        addListener();
        controlViewState(MoorEnumControlViewState.NORMAL);
        setTitleRightView();
        MoorPreLoader.listenData(preLoaderId
                , new OnEmojiCallBackListener()
                , new OnSatisfactionCallBackListener()
                , new OnIconCallBackListener()
                , new OnMsgCallBackListener());
        getHistoryMsg(false, "SdkNewChat");
        IMoorMsgSendObservable observable = IMoorMsgSendObservable.getInstance();
        //添加 消息发送观察者
        observable.registerObserver(iMoorMsgobserver);

        wsErrorDialog = MoorCommonBottomDialog.init(getResources().getString(R.string.moor_net_error_text)
                , getResources().getString(R.string.moor_net_error_reclient)
                , getResources().getString(R.string.moor_cancel));
        wsErrorDialog.setListener(netErrorDialogClick);
        hadInit = true;
        MoorEventBusUtil.registerEventBus(this);

        return moorChatView;
    }

    /**
     * 处理各类消息
     */
    public void handleMessage(Message msg) {
    }

    /**
     * 预处理表情数据回调
     */
    private class OnEmojiCallBackListener implements IMoorGroupedDataListener<List<MoorEmotion>> {
        @Override
        public void onDataArrived(List<MoorEmotion> data) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public String keyInGroup() {
            return MoorDemoConstants.MOOR_EMOJI_PRE_LOADER_KEY;
        }
    }

    /**
     * icon数据回调
     */
    private class OnIconCallBackListener implements IMoorGroupedDataListener<MoorIconUrlBean> {
        @Override
        public void onDataArrived(MoorIconUrlBean data) {
            String emojiUrl = data.getEmojiUrl();
            String keyboardUrl = data.getKeyboardUrl();
            String moreUrl = data.getMoreUrl();
            Drawable emojiDrawable;
            Drawable keyBoardDrawable;
            Drawable moreDrawable;
            if (TextUtils.isEmpty(emojiUrl)) {
                emojiDrawable = getActivity().getResources().getDrawable(R.drawable.moor_icon_chat_emoji_normal);
            } else {
                emojiDrawable = new BitmapDrawable(getActivity().getResources()
                        , MoorFileUtils.decodeSampledBitmapFromFile(emojiUrl, MoorPixelUtil.dp2px(30f), MoorPixelUtil.dp2px(30f)));
            }

            if (TextUtils.isEmpty(keyboardUrl)) {
                keyBoardDrawable = getActivity().getResources().getDrawable(R.drawable.moor_icon_keybord);
            } else {
                keyBoardDrawable = new BitmapDrawable(getActivity().getResources()
                        , MoorFileUtils.decodeSampledBitmapFromFile(keyboardUrl, MoorPixelUtil.dp2px(30f), MoorPixelUtil.dp2px(30f)));
            }


            if (TextUtils.isEmpty(moreUrl)) {
                moreDrawable = getActivity().getResources().getDrawable(R.drawable.moor_icon_more_add);
            } else {
                moreDrawable = new BitmapDrawable(getActivity().getResources()
                        , MoorFileUtils.decodeSampledBitmapFromFile(moreUrl, MoorPixelUtil.dp2px(30f), MoorPixelUtil.dp2px(30f)));
            }


            setSelectorDrawable(mChatEmojiNormal, emojiDrawable, keyBoardDrawable);
            setSelectorDrawable(mBtChooseMore, moreDrawable, moreDrawable);
        }

        @Override
        public String keyInGroup() {
            return MoorDemoConstants.MOOR_ICON_PRE_LOADER_KEY;
        }
    }

    /**
     * 预处理列表数据回调
     */
    private class OnMsgCallBackListener implements IMoorGroupedDataListener<List<MoorMsgBean>> {
        @Override
        public void onDataArrived(List<MoorMsgBean> data) {
            msgList.addAll(data);
            if (msgList.size() > 0) {
                msgPage++;
            }

            //onCreateView方法中调用，如果有横向快速按钮数据，则添加
            if (fastBtnBeans != null && fastBtnBeans.size() > 0) {
                Gson gson = new Gson();
                String content = gson.toJson(fastBtnBeans);
                MoorMsgBean msgBean = MoorMsgHelper.createFastbtnMsg(content);
                msgList.add(msgBean);
                MoorMsgDao.getInstance().addMsg(msgBean);
            }


            adapter.setItems(msgList);
            if (msgList.size() > 0) {
                /* 断言所有使用的类型都已注册 */
                assertAllRegistered(adapter, msgList);
            }

            adapter.setOnClickListener(adapterClick);
            mRvChat.setAdapter(adapter);
        }

        @Override
        public String keyInGroup() {
            return MoorDemoConstants.MOOR_MSG_PRE_LOADER_KEY;
        }
    }

    /**
     * 满意度配置数据回调
     */
    private class OnSatisfactionCallBackListener implements IMoorGroupedDataListener<MoorEvaluationBean> {
        @Override
        public void onDataArrived(MoorEvaluationBean data) {
            if (data != null) {
                moorEvaluationBean = data;
            }
        }

        @Override
        public String keyInGroup() {
            return MoorDemoConstants.MOOR_SATISFACTION_PRE_LOADER_KEY;
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        /*
        背景
         */
        rootLinearLayout = moorChatView.findViewById(R.id.rootView);
        /*
        标题栏
         */
        rlTitle = moorChatView.findViewById(R.id.rl_moorChatTitle);
        /*
        标题文字
         */
        tv_chat_title = moorChatView.findViewById(R.id.tv_chat_title);
        /*
        注销文字
         */
        mTvChatLogout = moorChatView.findViewById(R.id.tv_chat_logout);
        /*
        注销按钮和文字
         */
        llChatLogout = moorChatView.findViewById(R.id.ll_chat_logout);
        /*
        注销按钮图片
         */
        ivChatLogout = moorChatView.findViewById(R.id.iv_chat_logout);
        /*
        转人工 文字
         */
        tvChatConvert = moorChatView.findViewById(R.id.tv_chat_convert);
         /*
        转人工 按钮和文字
         */
        llChatConvert = moorChatView.findViewById(R.id.ll_chat_convert);
         /*
        转人工 按钮图片
         */
        ivChatConvert = moorChatView.findViewById(R.id.iv_chat_convert);
        /*
        关闭会话 文字
         */
        tvChatClose = moorChatView.findViewById(R.id.tv_chat_close);
         /*
        关闭会话 按钮和文字
         */
        llChatClose = moorChatView.findViewById(R.id.ll_chat_close);
         /*
        关闭会话 按钮图片
         */
        ivChatClose = moorChatView.findViewById(R.id.iv_chat_close);
        /*
        SwipeRefreshLayout
         */
        moorSwipe = moorChatView.findViewById(R.id.moor_swipe);
        /*
        列表
         */
        mRvChat = moorChatView.findViewById(R.id.rv_chat);
        /*
        面板组件
         */
        mPanelRoot = moorChatView.findViewById(R.id.panel_root);
        /*
        表情按钮
         */
        mChatEmojiNormal = moorChatView.findViewById(R.id.chat_emoji_normal);
        /*
        录音按钮
         */
        mChatSetModeVoice = moorChatView.findViewById(R.id.chat_set_mode_voice);
        /*
        键盘按钮
         */
        mChatSetModeKeyboard = moorChatView.findViewById(R.id.chat_set_mode_keyboard);
        /*
        输入框
         */
        mEtChatInput = moorChatView.findViewById(R.id.et_chat_input);
        /*
        表情组件
         */
        mMoorEmotionPagerView = moorChatView.findViewById(R.id.ep_view_pager);
        /*
        输入框和表情组件布局
         */
        slChatInput = moorChatView.findViewById(R.id.sl_chat_input);

        /*
        表情删除按钮
         */
        mIvDeleteEmoji = moorChatView.findViewById(R.id.iv_delete_emoji);
        llLayoutEmoji = moorChatView.findViewById(R.id.ll_layout_emoji);
        /*
        表情发送按钮
         */
        mTvSendEmoji = moorChatView.findViewById(R.id.tv_send_emoji);
         /*
        表情发送布局
         */
        slSendEmoji = moorChatView.findViewById(R.id.sl_send_emoji);
        /*
        更多按钮
         */
        mBtChooseMore = moorChatView.findViewById(R.id.bt_choose_more);
        /*
        发送按钮
         */
        mBtChatSend = moorChatView.findViewById(R.id.bt_chat_send);
        /*
        表情面板
         */
        mPanelEmotion = mPanelRoot.findViewById(R.id.panel_emotion);
        /*
        更多面板
         */
        mPanelMore = mPanelRoot.findViewById(R.id.panel_more);
        /*
        更多面板
         */
        mChatPanelView = mPanelRoot.findViewById(R.id.chat_panel_view);
        /*
        录音按钮
         */
        audioRecordButton = moorChatView.findViewById(R.id.btn_audio_record);
        /*
        底部横 按钮 整体布局
        */
        llBottomLayout = moorChatView.findViewById(R.id.ll_bottom_layout);
        /*
        底部横向滑动recycleView 按钮
        */
        rvBottomtag = moorChatView.findViewById(R.id.rv_bottom_tag);
        /*
        关键词联想
        */
        rvEditAssociate = moorChatView.findViewById(R.id.rv_edit_associate);
        /*
         * 顶部Tip include布局
         */
        moorChatTipView = moorChatView.findViewById(R.id.rl_moorChat_Top);
        /*
         * 顶部Tip 布局
         */
        rlTopTip = moorChatTipView.findViewById(R.id.rl_top_tip);
        /*
         * 顶部tip 文案
         */
        tvMoorTip = moorChatTipView.findViewById(R.id.tv_moor_tip);
        /*
         * 顶部Tip 布局隐藏
         */
        ivTipClose = moorChatTipView.findViewById(R.id.iv_tip_close);
        /*
         * 顶部Tip 左侧小喇叭
         */
        iv_tip_img = moorChatTipView.findViewById(R.id.iv_tip_img);
        /*
         * 底部排队数整体布局
         */
        llQueueLayout = moorChatView.findViewById(R.id.ll_queue_layout);
        /*
         * 排队数num
         */
        tvQueueNum = llQueueLayout.findViewById(R.id.tv_queue_num);
        /*
         *排队数右侧
         */
        tv_queue_num_right = llQueueLayout.findViewById(R.id.tv_queue_num_right);
        /*
         *排队数左侧
         */
        tv_queue_num_left = llQueueLayout.findViewById(R.id.tv_queue_num_left);
        /*
         * 退出排队icon
         */
        ivCancelQueue = llQueueLayout.findViewById(R.id.iv_cancel_queue);
        /*
         * 退出排队按钮
         */
        slCancelQueue = llQueueLayout.findViewById(R.id.sl_cancel_queue);


        moorSwipe.setProgressViewOffset(false, 0, MoorPixelUtil.dp2px(48));
        //底部列表分割线
        decor = new MoorSpaceItemDecoration(MoorPixelUtil.dp2px(10f), 0, MoorPixelUtil.dp2px(10f), 0);
    }

    /**
     * 获取历史消息
     *
     * @param loadMore true: 下拉加载更多
     * @param type     调用类型
     */
    private void getHistoryMsg(final boolean loadMore, final String type) {
        String serverTime = MoorSPUtils.getInstance().getString(MoorConstants.MOOR_SERVER_TIME);
        final String visitorId = infoBean.getVisitorId();
        final String account = infoBean.getAccount();
        String lastTime = "";
        if (loadMore) {
            lastTime = String.valueOf(msgList.get(0).getCreateTimestamp());
        } else {
            lastTime = serverTime;
        }
        MoorPostFormBuilder params = MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.GET_LIST_MSG)
                .tag(getActivity())
                .params(MoorHttpParams.getInstance().getListMsg(visitorId, account, lastTime, loadMore));
        params.build().execute(new MoorBaseCallBack<MoorNetBaseBean<MoorGetListMsgBean>>() {
            @Override
            public void onSuccess(MoorNetBaseBean<MoorGetListMsgBean> result, int id) {
                if (result.isSuccess()) {
                    final List<MoorMsgBean> messageList = MoorHttpParse.parseMsg(infoBean, result.getData().getMessageList());
                    if (messageList.size() > 0) {
                        MoorMsgDao.getInstance().updateMsgList(messageList);
                        Collections.reverse(messageList);
                        if (loadMore) {
                            final List<MoorMsgBean> sqlMsgData = MoorMsgDao.getInstance().queryMsgList(infoBean.getUserId(), msgPage);
                            if (sqlMsgData.size() == 20) {
                                msgPage++;
                            }
                            mRvChat.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (moorSwipe != null) {
                                        moorSwipe.setRefreshing(false);
                                    }
                                    msgList.addAll(0, sqlMsgData);
                                    if (adapter != null) {
                                        adapter.notifyItemRangeInserted(0, sqlMsgData.size());
                                    }
                                }
                            });
                        } else {
                            final List<MoorMsgBean> moorMsgBeans = MoorMsgDao.getInstance().queryMsgList(infoBean.getUserId(), 1, 5L);
                            mRvChat.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgList.clear();
                                    msgList.addAll(moorMsgBeans);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        //消费消息，消费后坐席侧显示已读
                        consumptionMsg();
                    } else {
                        mRvChat.post(new Runnable() {
                            @Override
                            public void run() {
                                if (moorSwipe != null) {
                                    moorSwipe.setRefreshing(false);
                                }
                            }
                        });
                    }

                } else {
                    MoorToastUtils.showShort(result.getMessage());
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                MoorToastUtils.showShort(e.getMessage());
            }
        });
    }

    private void getNewMsg(final List<MoorMsgBean> messageList) {
        if (messageList != null && messageList.size() > 0) {
            if (mRvChat != null) {
                mRvChat.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            msgList.addAll(messageList);
                            Collections.sort(msgList, new Comparator<MoorMsgBean>() {
                                @Override
                                public int compare(MoorMsgBean o1, MoorMsgBean o2) {
                                    if (o1.getCreateTimestamp() == o2.getCreateTimestamp()) {
                                        return 0;
                                    } else {
                                        return o1.getCreateTimestamp() > o2.getCreateTimestamp() ? 1 : -1;
                                    }
                                }
                            });
                            adapter.notifyItemChanged(msgList.size() - 1);
                            //消费消息，消费后坐席侧显示已读
                            consumptionMsg();
                        }
                    }
                }, 100);
                scrollToBottom();
            }
        }
    }


    /**
     * 应用UI配置项
     */
    private void installOptions(MoorOptions options) {
        mChatPanelView.buildPanels(this, MoorPanelDataHelper.getInstance().getPanelBeanList());
        int width = MoorScreenUtils.getScreenWidth(getActivity()) / 2 - MoorPixelUtil.dp2px(40f + 120f);
        llLayoutEmoji.setPadding(width, MoorPixelUtil.dp2px(23.5f), MoorPixelUtil.dp2px(8f), MoorPixelUtil.dp2px(23.5f));
        llLayoutEmoji.setBackgroundColor(MoorColorUtils.getColorWithAlpha(0.96f, getActivity().getResources().getColor(R.color.moor_color_eeeeee)));
        int titleBarBackgroundColor = options.getTitleBarBackgroundColor();
        if (titleBarBackgroundColor != 0) {
            rlTitle.setBackgroundColor(titleBarBackgroundColor);
        }
        int leftTitleBtnColor = options.getLeftTitleBtnColor();
        if (leftTitleBtnColor != 0) {
            mTvChatLogout.setTextColor(leftTitleBtnColor);
        }

        int chatBackgroundColor = options.getChatBackgroundColor();
        if (chatBackgroundColor != 0) {
            rootLinearLayout.setBackgroundColor(chatBackgroundColor);
        }

        String leftTitleBtnImgUrl = options.getLeftTitleBtnImgUrl();
        if (!TextUtils.isEmpty(leftTitleBtnImgUrl)) {
            ivChatLogout.setVisibility(View.VISIBLE);
            MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + leftTitleBtnImgUrl, ivChatLogout, MoorPixelUtil.dp2px(25f), MoorPixelUtil.dp2px(25f));
        } else {
            ivChatLogout.setVisibility(View.GONE);
        }

        String leftTitleBtnText = options.getLeftTitleBtnText();

        if (TextUtils.isEmpty(leftTitleBtnText)) {
            mTvChatLogout.setVisibility(View.GONE);
        } else {
            mTvChatLogout.setVisibility(View.VISIBLE);
            mTvChatLogout.setText(leftTitleBtnText);
        }

        String titleText = options.getSdkTitleBarText();
        if (!TextUtils.isEmpty(titleText)) {
            tv_chat_title.setText(titleText);
        }

        if (!options.isNeedSendImage()) {
            MoorPanelDataHelper.getInstance().removeItem(MoorPanelBean.TYPE.TYPE_IMAGE);
            mChatPanelView.updatePanel(MoorPanelDataHelper.getInstance().getPanelBeanList());
        }
        if (!options.isNeedSendFile()) {
            MoorPanelDataHelper.getInstance().removeItem(MoorPanelBean.TYPE.TYPE_FILE);
            mChatPanelView.updatePanel(MoorPanelDataHelper.getInstance().getPanelBeanList());
        }
        boolean useSendAction = options.isUseSystemKeyboardSendAction();
        if (useSendAction) {
            mEtChatInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
            mEtChatInput.setSingleLine();
        }
        //进入聊天页面 是否默认弹出键盘
        if (options.isAutoShowKeyboard()) {
            MoorSwitchConflictUtil.showKeyboard(mPanelRoot, mEtChatInput);
        }
        //添加顶部tip布局
        if (TextUtils.isEmpty(options.getTopNoticeText())) {
            moorChatTipView.setVisibility(View.GONE);
        } else {
            tvMoorTip.setText(options.getTopNoticeText());
            tvMoorTip.setTextColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
            rlTopTip.setBackgroundColor(MoorColorUtils.getColorWithAlpha(0.2f, options.getSdkMainThemeColor()));

            iv_tip_img.setColorFilter(Color.parseColor(options.getSdkMainThemeColor()));
            ivTipClose.setColorFilter(Color.parseColor(options.getSdkMainThemeColor()));

            moorChatTipView.setVisibility(View.VISIBLE);
        }

        //添加输入框hint文字
        if (!TextUtils.isEmpty(options.getInputViewHintText())) {
            mEtChatInput.setHint(options.getInputViewHintText());
        }
        //语音按钮图片
        String isVoiceBtnImgUrl = options.getIsVoiceBtnImgUrl();
        if (!TextUtils.isEmpty(isVoiceBtnImgUrl)) {
            MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + isVoiceBtnImgUrl, mChatSetModeVoice, MoorPixelUtil.dp2px(30f), MoorPixelUtil.dp2px(30f));
        }
        //发送按钮图片
        String sendMsgBtnImgUrl = options.getSendMsgBtnImgUrl();
        if (!TextUtils.isEmpty(sendMsgBtnImgUrl)) {
            MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + sendMsgBtnImgUrl, mBtChatSend, MoorPixelUtil.dp2px(30f), MoorPixelUtil.dp2px(30f));
        }
        //键盘按钮图片
        String showKeyboardImgUrl = options.getShowKeyboardImgUrl();
        if (!TextUtils.isEmpty(showKeyboardImgUrl)) {
            MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + showKeyboardImgUrl, mChatSetModeKeyboard, MoorPixelUtil.dp2px(30f), MoorPixelUtil.dp2px(30f));
        }

    }

    public void setSelectorDrawable(ImageView imageView, Drawable drawableNormal, Drawable drawableSelect) {

        StateListDrawable drawable = new StateListDrawable();

//        drawable.addState(new int[]{android.R.attr.state_checked}, drawableSelect);
//        drawable.addState(new int[]{-android.R.attr.state_checked}, drawableNormal);

        drawable.addState(new int[]{android.R.attr.state_selected}, drawableSelect);
        drawable.addState(new int[]{-android.R.attr.state_selected}, drawableNormal);

//        drawable.addState(new int[]{android.R.attr.state_focused}, drawableSelect);
//        drawable.addState(new int[]{-android.R.attr.state_focused}, drawableNormal);

        imageView.setBackground(drawable);

    }

    /**
     * 配置RecyclerView和adapter的参数属性
     */
    private void configProperties() {
        //获取状态栏高度，重新设置TitleBar高度
        int statusBarHeightCompat = MoorStatusBarUtil.getStatusBarHeight(mActivity);
        ViewGroup.LayoutParams layoutParams = rlTitle.getLayoutParams();
        layoutParams.height = statusBarHeightCompat + MoorPixelUtil.dp2px(50);
        rlTitle.setLayoutParams(layoutParams);
        //adapter
        adapter = new MoorMultiTypeAdapter();
        layoutManager = new MoorLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        //关闭默认动画来提升效率
//        mRvChat.getItemAnimator().setAddDuration(0);
//
//        mRvChat.getItemAnimator().setChangeDuration(0);
//
//        mRvChat.getItemAnimator().setMoveDuration(0);
//
//        mRvChat.getItemAnimator().setRemoveDuration(0);

        ((SimpleItemAnimator) mRvChat.getItemAnimator()).setSupportsChangeAnimations(false);
        //RecyclerView配置，增加流畅度
        mRvChat.setItemViewCacheSize(30);
        mRvChat.setDrawingCacheEnabled(true);
        mRvChat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        layoutManager.setStackFromEnd(true);
        mRvChat.setLayoutManager(layoutManager);
        MoorMultiBuilder.getInstance().setMoorOptions(options);
        MoorMultiBuilder.getInstance().build(adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {
        llChatLogout.setOnClickListener(this);
        llChatConvert.setOnClickListener(this);
        llChatClose.setOnClickListener(this);
        mBtChatSend.setOnClickListener(this);
        mChatSetModeKeyboard.setOnClickListener(this);
        mChatSetModeVoice.setOnClickListener(this);
        mIvDeleteEmoji.setOnClickListener(this);
        mTvSendEmoji.setOnClickListener(this);
        ivTipClose.setOnClickListener(this);
        final boolean useSendAction = options.isUseSystemKeyboardSendAction();
        if (useSendAction) {
            //使用键盘上的发送键来发送消息
            mEtChatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        sendChatInputMsg();
                    }
                    return true;
                }
            });
        }
        mEtChatInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoorSwitchConflictUtil.showKeyboard(mPanelRoot, mEtChatInput);
            }
        });
        // 监听输入框文字输入
        mEtChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!useSendAction) {
                    if (!TextUtils.isEmpty(s)) {
                        if (keyBoardIsShowing) {
                            mBtChatSend.setVisibility(View.VISIBLE);
                            mBtChooseMore.setVisibility(View.GONE);
                        } else {
                            mBtChatSend.setVisibility(View.GONE);

                            setMoreBtnVisiable();
                        }


                    } else {
                        setMoreBtnVisiable();
                        mBtChatSend.setVisibility(View.GONE);
                        mIvDeleteEmoji.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.moor_icon_delete_emoji));
                        mTvSendEmoji.setTextColor(getActivity().getResources().getColor(R.color.moor_color_4d999999));
                        slSendEmoji.setLayoutBackground(Color.WHITE);
                    }


                }
                if (keyBoardIsShowing) {
                    initAssociateData(s.toString());
                }
                int lineCount = mEtChatInput.getLineCount();
                //单行
                if (lineCount == 1) {
                    slChatInput.setCornerRadius(MoorPixelUtil.dp2px(22f));
                    slChatInput.invalidate();
                } else if (lineCount > 1) {
                    //多行
                    slChatInput.setCornerRadius(MoorPixelUtil.dp2px(10f));
                    slChatInput.invalidate();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    rvEditAssociate.setVisibility(View.GONE);
                }
                if (infoBean.isVisitorTypeNotice()) {
                    typeNotice(s.toString());
                }
            }
        });
        //输入框焦点改变监听
        mEtChatInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mChatEmojiNormal.setSelected(false);
                    mBtChooseMore.setSelected(false);
                    MoorSwitchConflictUtil.showKeyboard(mPanelRoot, mEtChatInput);
                } else {
                    rvEditAssociate.setVisibility(View.GONE);
                }
            }
        });

        //列表滚动到底部不彻底的解决办法
        mRvChat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mRvChat.getAdapter() != null) {
                    boolean canScroll = mRvChat.computeVerticalScrollRange() > mRvChat.computeVerticalScrollExtent();
                    if (canScroll && !layoutManager.getStackFromEnd()) {
                        layoutManager.setStackFromEnd(true);
                        return;
                    }
                    if (!canScroll && layoutManager.getStackFromEnd()) {
                        layoutManager.setStackFromEnd(false);
                    }
                }
                mRvChat.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //点击列表，收起键盘/面板
        mRvChat.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hidePanelAndKeyboard();
                }
                return false;
            }
        });

        //下拉加载更多消息
        moorSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHistoryMsg(true, "OnRefresh");
                    }
                }, 200);
            }
        });

        //语音消息 完成监听
        audioRecordButton.setRecordFinishListener(new MoorAudioRecorderButton.RecorderFinishListener() {
            @Override
            public void onRecordFinished(float mTime, String filePath, String pcmFilePath) {
                if (!MoorFileUtils.isFileExists(filePath)) {
                    return;
                }
                dealVoiceCallBack(filePath);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_chat_logout) {
            if (MoorAntiShakeUtils.getInstance().check()) {
                return;
            }
            //注销按钮
            mActivity.finish();
        } else if (v.getId() == R.id.ll_chat_convert) {
            if (MoorAntiShakeUtils.getInstance().check()) {
                return;
            }
            if (!checkNetDialog()) {
                return;
            }
            if (MoorConstants.NOW_ROBOT_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
                //机器人状态下，为转人工
                convertManual("");
            }
        } else if (v.getId() == R.id.ll_chat_close) {
            if (MoorAntiShakeUtils.getInstance().check()) {
                return;
            }
            if (!checkNetDialog()) {
                return;
            }
            if (MoorConstants.NOW_CLAIM_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
                //坐席状态下 为关闭会话
                closeSession();
            }
        } else if (v.getId() == R.id.bt_chat_send) {
            //发送消息
            sendChatInputMsg();
        } else if (v.getId() == R.id.chat_set_mode_keyboard) {
            //键盘按钮
            controlViewState(MoorEnumControlViewState.NORMAL);
            mEtChatInput.requestFocus();
        } else if (v.getId() == R.id.chat_set_mode_voice) {
            //录音按钮
            hidePanelAndKeyboard();
            MoorPermissionUtil.checkPermission(MoorChatFragment.this, new OnRequestCallback() {
                @Override
                public void requestSuccess() {
                    controlViewState(MoorEnumControlViewState.RECORD);
                }
            }, MoorPermissionConstants.RECORD_AUDIO);
        } else if (v.getId() == R.id.iv_delete_emoji) {
            //删除表情按钮
            int keyCode = KeyEvent.KEYCODE_DEL;
            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
            KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
            mEtChatInput.onKeyDown(keyCode, keyEventDown);
            mEtChatInput.onKeyUp(keyCode, keyEventUp);
        } else if (v.getId() == R.id.tv_send_emoji) {
            if (MoorAntiShakeUtils.getInstance().check()) {
                return;
            }
            //发送表情按钮
            sendChatInputMsg();
        } else if (v.getId() == R.id.iv_tip_close) {
            //隐藏顶部Tip
            moorChatTipView.setVisibility(View.GONE);
        }
    }

    /**
     * 收起键盘
     */
    private void hidePanelAndKeyboard() {
        MoorSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
        mEtChatInput.clearFocus();
        mChatEmojiNormal.setSelected(false);
        mBtChooseMore.setSelected(false);

    }

    /**
     * 键盘面板相关
     */
    private void attachPanel() {
        MoorKeyboardUtil.attach(mActivity, mPanelRoot, new MoorKeyboardUtil.OnKeyboardShowingListener() {
            @Override
            public void onKeyboardShowing(boolean isShowing) {
                keyBoardIsShowing = isShowing;
                if (isShowing) {
                    mPanelRoot.setVisibility(View.INVISIBLE);
                    if (mEtChatInput.getText().toString().length() > 0) {
                        mBtChatSend.setVisibility(View.VISIBLE);
                        mBtChooseMore.setVisibility(View.GONE);
                    }
                    scrollToBottom();
                } else {
                    if (mEtChatInput.getText().toString().length() > 0) {
                        mBtChatSend.setVisibility(View.GONE);
                        setMoreBtnVisiable();
                    }
                }
            }
        });

        MoorSwitchConflictUtil.attach(mPanelRoot, mEtChatInput,
                new MoorSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(View v, int switchToPanel) {
                        if (switchToPanel == 0) {
                            mEtChatInput.requestFocus();
                        } else {
                            mEtChatInput.clearFocus();
                            layoutManager.scrollToPosition(msgList.size() - 1);
                        }
                        if (v.getId() == R.id.chat_emoji_normal) {
                            mChatEmojiNormal.setSelected(switchToPanel != 0);
                            mBtChooseMore.setSelected(false);
                            controlViewState(MoorEnumControlViewState.EMOJI);
                            if (hadInit) {
                                mMoorEmotionPagerView.buildEmotionViews(mEtChatInput
                                        , MoorEmojiBitmapUtil.getMoorEmotions()
                                        , MoorScreenUtils.getScreenWidth(mActivity)
                                        , MoorKeyboardUtil.getValidPanelHeight(mActivity), new IMoorEmojiClickListener() {
                                            @Override
                                            public void onItemClick() {
                                                mIvDeleteEmoji.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.moor_icon_delete_emoji_select));
                                                mTvSendEmoji.setTextColor(getActivity().getResources().getColor(R.color.moor_color_ffffff));
                                                slSendEmoji.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
                                            }
                                        });
                                hadInit = false;
                            }

                        } else if (v.getId() == R.id.bt_choose_more) {
                            mChatEmojiNormal.setSelected(false);
                            mBtChooseMore.setSelected(switchToPanel != 0);
                            controlViewState(MoorEnumControlViewState.PANEL);
                            evaluateBtnVisible();

                        }
                    }
                },
                new MoorSwitchConflictUtil.SubPanelAndTrigger(mPanelMore, mBtChooseMore),
                new MoorSwitchConflictUtil.SubPanelAndTrigger(mPanelEmotion, mChatEmojiNormal)
        );

    }

    /**
     * 添加表情按钮
     */
    private void evaluateBtnVisible() {
        if (MoorSPUtils.getInstance().getBoolean(MoorConstants.MOOR_SHOW_CSRBTN)) {
            MoorPanelDataHelper.getInstance().removeItem(MoorPanelBean.TYPE.TYPE_EVALUATE);
            MoorPanelDataHelper.getInstance().addItem(MoorPanelDataHelper.getInstance().createEvaluateItem());
            mChatPanelView.updatePanel(MoorPanelDataHelper.getInstance().getPanelBeanList());
        } else {
            MoorPanelDataHelper.getInstance().removeItem(MoorPanelBean.TYPE.TYPE_EVALUATE);
            mChatPanelView.updatePanel(MoorPanelDataHelper.getInstance().getPanelBeanList());
        }
    }

    /**
     * 滚动列表到底部
     */
    private void scrollToBottom() {
        if (mRvChat != null) {
            mRvChat.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutManager.scrollToPosition(msgList.size() - 1);
                }
            }, 200);
        }
    }

    /**
     * 设置页面初始值；语音、更多面板、键盘按钮
     *
     * @param state
     */
    private void controlViewState(MoorEnumControlViewState state) {
        switch (state) {
            case NORMAL:
            case PANEL:
            default:
                mChatSetModeVoice.setVisibility(View.VISIBLE);
                slChatInput.setVisibility(View.VISIBLE);
                mBtChooseMore.setVisibility(View.VISIBLE);
                mChatSetModeKeyboard.setVisibility(View.GONE);
                audioRecordButton.setVisibility(View.GONE);
                break;
            case EMOJI:
                mChatSetModeVoice.setVisibility(View.VISIBLE);
                slChatInput.setVisibility(View.VISIBLE);
                mBtChooseMore.setVisibility(View.VISIBLE);
                mChatSetModeKeyboard.setVisibility(View.GONE);
                audioRecordButton.setVisibility(View.GONE);
                mBtChatSend.setVisibility(View.GONE);
                break;
            case RECORD:
                mChatSetModeVoice.setVisibility(View.GONE);
                slChatInput.setVisibility(View.GONE);
                mBtChooseMore.setVisibility(View.VISIBLE);
                mChatSetModeKeyboard.setVisibility(View.VISIBLE);
                audioRecordButton.setVisibility(View.VISIBLE);
                break;
        }

        if (mChatEmojiNormal.getVisibility() == View.VISIBLE) {
            boolean showEmojiButton = options.isEmojiBtnShow();
            mChatEmojiNormal.setVisibility(showEmojiButton ? View.VISIBLE : View.GONE);
        }
        if (mChatSetModeVoice.getVisibility() == View.VISIBLE) {
            boolean showVoiceButton = options.isVoiceBtnShow();
            mChatSetModeVoice.setVisibility(showVoiceButton ? View.VISIBLE : View.GONE);
        }
        if (mBtChooseMore.getVisibility() == View.VISIBLE) {
            setMoreBtnVisiable();
        }
    }

    /**
     * 处理加号是否显示
     */
    private void setMoreBtnVisiable() {
        boolean showPanelButton = options.isShowPanelButton();
        if (!MoorManager.getInstance().getChatStatus().equals(MoorConstants.NOW_CLAIM_STATUS)) {
            if (options.isRobotShowPanelButton()) {
                mBtChooseMore.setVisibility(showPanelButton ? View.VISIBLE : View.GONE);
            } else {
                //非人工状态下 不显示加号按钮
                mBtChooseMore.setVisibility(options.isRobotShowPanelButton() ? View.VISIBLE : View.GONE);
            }
        } else {
            mBtChooseMore.setVisibility(showPanelButton ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 发送文本框中的消息
     */
    private void sendChatInputMsg() {
        String content = mEtChatInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        mEtChatInput.setText(null);
        sendTextMsg(content);
    }

    /**
     * 通用发送文本消息
     */
    private void sendTextMsg(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        MoorMsgBean textMsg = MoorMsgHelper.createTextMsg(text);
        sendMsgToPage(textMsg);
        jobManagerSendMsg(textMsg);
    }


    /**
     * 消息重发
     */
    private void reSendMsg(MoorMsgBean msgBean) {
        if (msgBean != null) {
            //更新列表数据
            int index = 0;
            for (int i = msgList.size() - 1; i >= 0; i--) {
                if (msgList.get(i).getMessageId().equals(msgBean.getMessageId())) {
                    msgList.get(i).setStatus(MoorChatMsgType.MSG_TYPE_STATUS_SENDING)
                            .setCreateTimestamp(System.currentTimeMillis());
                    index = i;
                    break;
                }
            }
            Collections.sort(msgList, new Comparator<MoorMsgBean>() {
                @Override
                public int compare(MoorMsgBean o1, MoorMsgBean o2) {
                    if (o1.getCreateTimestamp() == o2.getCreateTimestamp()) {
                        return 0;
                    } else {
                        // 从小到大
                        return o1.getCreateTimestamp() > o2.getCreateTimestamp() ? 1 : -1;
                    }

                }
            });
            //刷新页面
            if (adapter != null) {
                adapter.notifyItemChanged(index);
            }
            //更新数据库
            MoorMsgDao.getInstance().updateMoorMsgBeanToDao(msgBean);
            if (msgBean.getContentType().equals(MoorChatMsgType.MSG_TYPE_FILE)
                    || msgBean.getContentType().equals(MoorChatMsgType.MSG_TYPE_IMAGE)
                    || msgBean.getContentType().equals(MoorChatMsgType.MSG_TYPE_VOICE)) {
                dealUpload(msgBean.getFileLocalPath(), msgBean, msgBean.getContentType().equals(MoorChatMsgType.MSG_TYPE_FILE));
            } else {
                jobManagerSendMsg(msgBean);
            }

        }
    }

    /**
     * 消息队列发送消息
     *
     * @param msgBean
     */
    private void jobManagerSendMsg(MoorMsgBean msgBean) {
        JobManager jobManager = MoorUtils.getJobManager();
        MoorMessageJob messageJob = new MoorMessageJob(msgBean);
        jobManager.addJobInBackground(messageJob);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        // 当屏幕分屏/多窗口变化时回调
        MoorSwitchConflictUtil.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
        if (MoorManager.getInstance().isHavaNetStatus()) {
            if (!MoorManager.getInstance().checkWs()) {
                MoorManager.getInstance().reConnectMoorServer();
            } else {
                checkNetDialog();
            }
            //消费消息
            consumptionMsg();
        } else {
            checkNetDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPanelRoot.recordKeyboardStatus(mActivity.getWindow());
        isFront = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        MoorMediaPlayTools.getInstance().stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingDialog = null;
        if (audioRecordButton != null) {
            audioRecordButton.recycle();
            audioRecordButton.cancelListener();
        }
        IMoorMsgSendObservable.getInstance().unregisterObserver(iMoorMsgobserver);
        MoorPreLoader.destroy(preLoaderId);
        MoorMediaPlayTools.getInstance().recycle();
        if (chatHandler != null) {
            chatHandler.removeCallbacksAndMessages(null);
        }
        MoorEventBusUtil.removeAllStickyEvents();
        MoorEventBusUtil.unregisterEventBus(this);
        MoorHttpUtils.getInstance().cancelTag(getActivity());
        MoorTextParseUtil.getInstance().clear();
        MoorManager.getInstance().exitSdk();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MoorKeyboardUtil.detach(getActivity());
        MoorUploadHelper.getInstance().setCancelled(true);
    }

    @Override
    public void onPanelClickListener(MoorPanelBean panelBean) {
        if (!checkNetDialog()) {
            return;
        }
        switch (panelBean.getType()) {
            case TYPE_CAMERA:
                MoorPermissionUtil.checkPermission(this, new OnRequestCallback() {
                    @Override
                    public void requestSuccess() {
                        MoorTakePicturesHelper.getInstance().openCamera(MoorChatFragment.this, MoorDemoConstants.CAMERA_ACTIVITY_REQUEST_CODE);
                    }
                }, MoorPermissionConstants.CAMERA);
                break;
            case TYPE_IMAGE:
                MoorPermissionUtil.checkPermission(this, new OnRequestCallback() {
                    @Override
                    public void requestSuccess() {
                        openAlbum();
                    }
                }, MoorPermissionConstants.STORE);
                break;
            case TYPE_FILE:
                MoorPermissionUtil.checkPermission(this, new OnRequestCallback() {
                    @Override
                    public void requestSuccess() {
                        openFile();
                    }
                }, MoorPermissionConstants.STORE);
                break;

            case TYPE_EVALUATE:
                if (MoorConstants.NOW_ROBOT_STATUS.equals(MoorManager.getInstance().getChatStatus())) {

                } else if (MoorConstants.NOW_CLAIM_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
                    openEvaluationDialog("", null, "");
                }
                break;

            case TYPE_ORDERCARD:
                sendOrderCard();
                break;
            case TYPE_UNBLOCK:
                unblockBlackList();
                break;
            default:
                break;
        }
    }


    /**
     * 收到 Socket 新消息
     *
     * @param moorSocketNewMsg
     */
    @Subscribe
    public void handleMoorSocketNewMsg(MoorSocketNewMsg moorSocketNewMsg) {
        //socket收到新消息
        getNewMsg(moorSocketNewMsg.getMessageList());
    }


    /**
     * 收到转人工按钮 通知
     */
    @Subscribe
    public void handleMoorManualButton(MoorManualEvent moorManualEvent) {
        setTitleRightView();
    }

    /**
     * 收到 坐席关闭会话消息
     *
     * @param closeSessionEvent getAutoCloseMessage()为会话关闭的提示语
     */
    @Subscribe
    public void handleMoorSocketCloseSession(MoorCloseSessionEvent closeSessionEvent) {
        String msg = getString(R.string.moor_csr_close_session);
        if (!TextUtils.isEmpty(closeSessionEvent.getAutoCloseMessage())) {
            msg = closeSessionEvent.getAutoCloseMessage();
        }
        MoorMsgBean csrThankMsg = MoorMsgHelper.createSysMsg(msg, closeSessionEvent.getTimeTamp());
        llBottomLayout.setVisibility(View.GONE);
        llQueueLayout.setVisibility(View.GONE);
        sendMsgToPage(csrThankMsg);
        //如果SatisfactionMessageId！=null 需要进行满意度评价，当做msgId提交满意度评价
        if (!TextUtils.isEmpty(closeSessionEvent.getSatisfactionMessageId())) {
            openEvaluationDialog(closeSessionEvent.getSatisfactionMessageId(), null, closeSessionEvent.getSessionId());
        }
        setTitleRightView();
        setMoreBtnVisiable();
        updateUseFulState();
    }

    /**
     * getag收到机器人底部菜单
     *
     * @param moorBottomListEvent
     */
    @Subscribe(sticky = true)
    public void handleMoorBottomList(MoorBottomListEvent moorBottomListEvent) {
        final ArrayList<MoorBottomBtnBean> bottomListBeans = moorBottomListEvent.getBottomListBeans();
        if (bottomListBeans != null || bottomListBeans.size() > 0) {
            chatHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initBottomList(bottomListBeans);
                }
            }, 500);
        }
        MoorEventBusUtil.removeStickyEvent(moorBottomListEvent);
    }

    /**
     * 收到坐席接入会话通知
     */
    @Subscribe
    public void handleMoorClaim(MoorClaimEvent claimEvent) {
        if (claimEvent != null) {
            if (claimEvent.getMoorMsgBean() != null) {
                addMsgToPage(claimEvent.getMoorMsgBean());
            }
        }
        //有坐席接入会话后 右上角转人工按钮切换为:关闭会话按钮
        llBottomLayout.setVisibility(View.GONE);
        setTitleRightView();
        setMoreBtnVisiable();
    }

    /**
     * 收到机器人接入会话通知
     */
    @Subscribe
    public void handleMoorRobot(MoorRobotEvent robotEvent) {
        if (robotEvent != null) {
            if (robotEvent.getMoorMsgBean() != null) {
                addMsgToPage(robotEvent.getMoorMsgBean());
            }
            setMoreBtnVisiable();
            if (llQueueLayout != null) {
                llQueueLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 收到xbot quick 数据
     */
    @Subscribe
    public void handleMoorXbotQuickEvent(MoorXbotQuickEvent xbotQuickEvent) {
        if (xbotQuickEvent != null) {
            if (xbotQuickEvent.getMoorMsgBean() != null) {
                //刷新页面
                if (adapter != null) {
                    Iterator<MoorMsgBean> iterator = msgList.iterator();
                    while (iterator.hasNext()) {
                        MoorMsgBean i = iterator.next();
                        if (i.getContentType().equals(MoorChatMsgType.MSG_TYPE_LIST_XBOT_QUICKMENU)) {
                            iterator.remove();
                        }
                    }
                    addMsgToPage(xbotQuickEvent.getMoorMsgBean());
                }
            }
        }
    }

    /**
     * 收到清空状态
     */
    @Subscribe()
    public void handleMoorClearStatus(MoorClearStatusEvent moorClearStatusEvent) {
        dealQueueStatus(null);
        setTitleRightView();
    }

    /**
     * 收到排队数消息
     *
     * @param queueEvent
     */
    @Subscribe(sticky = true)
    public void handleMoorQueue(MoorQueueEvent queueEvent) {
        dealQueueStatus(queueEvent);
        MoorEventBusUtil.removeStickyEvent(queueEvent);
    }


    /**
     * 处理排队状态
     */
    private void dealQueueStatus(MoorQueueEvent queueEvent) {
        if (MoorConstants.NOW_QUEUE_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
            int num = queueEvent.getQueueNum();
            String queueTips = queueEvent.getQueueTips();
            boolean isShowCancelQueue = queueEvent.isShowCancelQueue();
            if (num > 0) {
                //有排队数 隐藏底部快捷按钮
                llBottomLayout.setVisibility(View.GONE);
                setTitleRightView();//隐藏右上角按钮
                llQueueLayout.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(queueTips)) {
                    try {
                        String[] split = queueTips.split("\\{num\\}");
                        if (split.length == 2) {
                            tv_queue_num_left.setText(split[0]);
                            tv_queue_num_right.setText(split[1]);
                            tvQueueNum.setVisibility(View.VISIBLE);
                            tv_queue_num_right.setVisibility(View.VISIBLE);
                        } else {
                            tv_queue_num_left.setText(queueTips);
                            tvQueueNum.setVisibility(View.GONE);
                            tv_queue_num_right.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                tvQueueNum.setTextColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
                tvQueueNum.setText(" " + num + " ");

                ivCancelQueue.setBackgroundColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
                slCancelQueue.setVisibility(isShowCancelQueue ? View.VISIBLE : View.GONE);
                if (isShowCancelQueue) {
                    //退出排队接口
                    slCancelQueue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MoorHttpUtils.post()
                                    .url(MoorUrlManager.BASE_URL + MoorUrlManager.CANCEL_QUEUE)
                                    .params(MoorHttpParams.getInstance().cancelQueue())
                                    .build()
                                    .execute(new MoorBaseCallBack<MoorNetBaseBean<MoorServerTimeBean>>() {
                                        @Override
                                        public void onSuccess(MoorNetBaseBean<MoorServerTimeBean> result, int id) {
                                            if (result.isSuccess()) {
                                                final String time = result.getData().getTime();
                                                llQueueLayout.setVisibility(View.GONE);
                                                chatHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        MoorMsgBean cancelQueueMsg = MoorMsgHelper.createCancelQueueMsg(time + " " + getString(R.string.moor_cancel_queue_success));
                                                        sendMsgToPage(cancelQueueMsg);
                                                    }
                                                }, 200);

                                            }
                                        }
                                    });
                        }
                    });
                }

            }
        } else {
            llQueueLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 收到newchat成功通知
     */
    @Subscribe
    public void handleMoorSdkNewChat(MoorNewChatEvent newChatEvent) {
        if (newChatEvent.isNewChatOK()) {
            if (loadingDialog != null) {
                loadingDialog.dismissDialog();
            }
            if (msgPage > 2) {
                msgPage = 2;
            }
            getHistoryMsg(false, "SdkNewChat");
        } else {
            MoorToastUtils.showShort(getResources().getString(R.string.moor_newchat_error));
            getActivity().finish();
        }
    }

    /**
     * 机器人点击转人工
     *
     * @param agentEvent
     */
    @Subscribe
    public void handleTransferAgent(MoorTransferAgentEvent agentEvent) {
        convertManual(agentEvent.getKey());
    }

    /**
     * 收到留言状态
     */
    @Subscribe
    public void handleMoorLeaveEvent(MoorLeaveEvent moorLeaveEvent) {
        setTitleRightView();
    }

    /**
     * 收到坐席撤回消息
     *
     * @param withdrawMessage
     */
    @Subscribe
    public void handleMoorWithdrawMessage(MoorWithdrawMessage withdrawMessage) {
        String messageId = withdrawMessage.getMessageId();
        if (!TextUtils.isEmpty(messageId)) {
            if (adapter != null) {
                for (int i = 0; i < msgList.size(); i++) {
                    MoorMsgBean moorMsgBean = msgList.get(i);
                    if (moorMsgBean.getMessageId().equals(messageId)) {
                        StringBuilder builder = new StringBuilder();
                        builder.append(withdrawMessage.getCreateTime());
                        builder.append(" " + moorMsgBean.getUserName());
                        MoorInfoBean infoBean = MoorInfoDao.getInstance().queryInfo();
                        if (!TextUtils.isEmpty(infoBean.getClaimUserName())) {
                            builder.append(" [" + infoBean.getClaimUserName() + "]");
                        }
                        builder.append(" " + getString(R.string.moor_withdraw_msg));
                        moorMsgBean.setContentType(MoorChatMsgType.MSG_TYPE_SYSTEM)
                                .setContent(builder.toString());

                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 收到坐席消费消息
     */
    @Subscribe
    public void handleMoorReadStatus(MoorReadStatusEvent moorReadStatusEvent) {
        //右侧消息全部变为已读
        MoorMsgDao.getInstance().updateUnReadToRead();
        for (MoorMsgBean bean : msgList) {
            bean.setDealCustomerMsg(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 被拉黑了，返回展示语
     */
    @Subscribe(sticky = true)
    public void handleMoorBlackListEvent(MoorBlackListEvent blackListEvent) {
        MoorMsgBean blackTipsMsg = MoorMsgHelper.createBlackTipsMsg(blackListEvent.getContent());
        sendMsgToPage(blackTipsMsg);
        MoorEventBusUtil.removeStickyEvent(blackListEvent);
    }

    /**
     * 展示解封按钮
     */
    @Subscribe(sticky = true)
    public void handleMoorShowUnblockButtonEvent(MoorShowUnblockButtonEvent showUnblockButtonEvent) {
        if (!TextUtils.isEmpty(showUnblockButtonEvent.getContent())) {
            unblockContent = showUnblockButtonEvent.getContent();
        }
        MoorPanelDataHelper.getInstance().removeItem(MoorPanelBean.TYPE.TYPE_UNBLOCK);
        MoorPanelDataHelper.getInstance().addItem(MoorPanelDataHelper.getInstance().createUnblockItem());
        mChatPanelView.updatePanel(MoorPanelDataHelper.getInstance().getPanelBeanList());
        MoorEventBusUtil.removeStickyEvent(showUnblockButtonEvent);
    }

    /**
     * 打开本地相册
     */
    public void openAlbum() {
        try {
            Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            picture.setType("image/*");
            startActivityForResult(picture, MoorDemoConstants.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            MoorToastUtils.showShort(getString(R.string.moor_no_photo_album));
        }
    }

    /**
     * 打开文件选择
     */
    private void openFile() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, MoorDemoConstants.PICK_FILE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            MoorToastUtils.showShort(getString(R.string.moor_no_file_manager));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hidePanelAndKeyboard();

        if (requestCode == MoorDemoConstants.PICK_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            dealPicCallBack(data);
        } else if (requestCode == MoorDemoConstants.PICK_FILE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            dealFileCallBack(data);
        } else if (requestCode == MoorDemoConstants.CAMERA_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                dealCameraCallback();
            } else {
                //删除uri
                MoorTakePicturesHelper.getInstance().deleteUri(mActivity);
            }
        }
    }

    /**
     * 拍照返回处理
     */
    private void dealCameraCallback() {
        String realPath = "";
        if (MoorSdkVersionUtil.over29()) {
            // Android 10 使用图片uri加载
            Uri uri = MoorTakePicturesHelper.getInstance().getCameraUri();
            if (uri != null) {
                realPath = MoorFileUtils.getRealPathFromUri(mActivity, uri);
            }

        } else {
            // 使用图片路径加载
            realPath = MoorTakePicturesHelper.getInstance().getCameraImagePath();
        }
        if (TextUtils.isEmpty(realPath)) {
            return;
        }
        if (!TextUtils.isEmpty(realPath)) {
            final MoorMsgBean msgBean = MoorMsgHelper.createImageMsg(realPath);
            sendMsgToPage(msgBean);
            dealUpload(realPath, msgBean, false);
        }

    }

    /**
     * 选择图片返回处理
     */
    private void dealPicCallBack(Intent data) {
        Uri uri = data.getData();
        String realPath = MoorFileUtils.getRealPathFromUri(mActivity, uri);
        final MoorMsgBean msgBean = MoorMsgHelper.createImageMsg(realPath);
        if (!TextUtils.isEmpty(realPath)) {
            sendMsgToPage(msgBean);
            dealUpload(realPath, msgBean, false);
        }
    }

    /**
     * 语音发送回调
     *
     * @param realPath
     */
    private void dealVoiceCallBack(String realPath) {
        final MoorMsgBean voiceMsg = MoorMsgHelper.createVoiceMsg(realPath);
        sendMsgToPage(voiceMsg);
        dealUpload(realPath, voiceMsg, false);
    }

    /**
     * 选择文件返回处理
     */
    private void dealFileCallBack(Intent data) {
        Uri uri = data.getData();
        String realPath = MoorFileUtils.getRealPathFromUri(mActivity, uri);
        if (TextUtils.isEmpty(realPath)) {
            return;
        }
        String fileName = MoorFileUtils.getFileName(realPath);
        String fileSize = MoorFileUtils.readFileSize(realPath);
        String fileType = MoorFileFormatUtils.getFileType(fileName);

        final MoorMsgBean fileMsg = MoorMsgHelper.createFileMsg(fileName, fileSize, fileType, realPath);

        sendMsgToPage(fileMsg);
        dealUpload(realPath, fileMsg, true);
    }

    /**
     * 图片、语音、拍照、附件上传方法
     *
     * @param realPath
     * @param msgBean
     */
    private void dealUpload(final String realPath, final MoorMsgBean msgBean, final boolean isFile) {
        final int[] oldIndex = {-1};
        final int[] indexOf = {-1};
        String fileName = MoorFileUtils.getFileName(realPath);
        MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.GET_TOKEN)
                .params(MoorHttpParams.getInstance().getToken(fileName))
                .build().execute(new MoorBaseCallBack<MoorNetBaseBean<MoorUploadBean>>() {
            @Override
            public void onSuccess(MoorNetBaseBean<MoorUploadBean> result, int id) {
                if (result.isSuccess()) {
                    MoorUploadHelper.getInstance().dealUpload(realPath, result.getData(), new IMoorUploadCallBackListener() {
                        @Override
                        public void onUploadSuccess(String fileUrl) {
                            msgBean.setContent(fileUrl);
                            if (isFile) {
                                msgBean.setFileProgress(100);
                            }
                            jobManagerSendMsg(msgBean);
                        }

                        @Override
                        public void onUploadFailed() {
                            dealUploadFailed(msgBean, oldIndex, indexOf);
                        }

                        @Override
                        public void onUploadProgress(int progress) {
                            if (isFile) {
                                msgBean.setFileProgress(progress);

                                //更新列表数据
                                if (oldIndex[0] != msgList.size()) {
                                    oldIndex[0] = msgList.size();
                                    indexOf[0] = msgList.indexOf(msgBean);
                                }
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemChanged(indexOf[0], MoorDemoConstants.MOOR_PAYLOAD_FILE_SEND_PROGRESS);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    dealUploadFailed(msgBean, oldIndex, indexOf);
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                dealUploadFailed(msgBean, oldIndex, indexOf);
            }
        });
    }

    /**
     * 上传失败处理
     *
     * @param msgBean
     * @param oldIndex
     * @param indexOf
     */
    private void dealUploadFailed(MoorMsgBean msgBean, int[] oldIndex, final int[] indexOf) {
        msgBean.setStatus(MoorChatMsgType.MSG_TYPE_STATUS_FAILURE);
        //更新列表数据
        if (oldIndex[0] != msgList.size()) {
            oldIndex[0] = msgList.size();
            indexOf[0] = msgList.indexOf(msgBean);
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(indexOf[0]);
            }
        });
        MoorMsgDao.getInstance().addMsg(msgBean);
    }

    /**
     * 发送消息到列表展示并添加到数据库
     *
     * @param msgBean
     */
    private void sendMsgToPage(MoorMsgBean msgBean) {
        msgList.add(msgBean);
        adapter.setItems(msgList);
        MoorMsgDao.getInstance().addMsg(msgBean);
        //刷新页面
        if (adapter != null) {
            adapter.notifyItemChanged(msgList.size() - 1);
        }
        scrollToBottom();
    }

    /**
     * 添加消息到列表展示
     *
     * @param msgBean
     */
    private void addMsgToPage(MoorMsgBean msgBean) {
        //刷新页面
        if (adapter != null) {
            msgList.add(msgBean);
            Collections.sort(msgList, new Comparator<MoorMsgBean>() {
                @Override
                public int compare(MoorMsgBean o1, MoorMsgBean o2) {
                    if (o1.getCreateTimestamp() == o2.getCreateTimestamp()) {
                        return 0;
                    } else {
                        return o1.getCreateTimestamp() > o2.getCreateTimestamp() ? 1 : -1;
                    }
                }
            });
            adapter.setItems(msgList);
            adapter.notifyItemChanged(msgList.size() - 1);
//            adapter.notifyDataSetChanged();
        }
        scrollToBottom();
    }

    /**
     * 附件点击事件
     *
     * @param item
     * @param obj
     */
    private void dealFileClickEvent(final MoorMsgBean item, Object[] obj) {
        String url = (String) obj[0];
        final int position = (int) obj[1];
        if (url.startsWith("http")) {
            MoorDownLoadUtils.loadFile(url, item.getFileName(), new IMoorOnDownloadListener() {
                @Override
                public void onDownloadStart() {

                }

                @Override
                public void onDownloadSuccess(String filePath) {
                    boolean saveFile = MoorFileUtils.saveFile(getActivity(), new File(filePath), item.getFileName());
                    if (saveFile) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    item.setFileProgress(100)
                                            .setFileLocalPath(MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE) + item.getFileName());
                                    MoorMsgDao.getInstance().updateMoorMsgBeanToDao(item);
                                    adapter.notifyItemChanged(position);
                                }
                            });
                        } catch (Exception e) {

                        }
                    }

                }

                @Override
                public void onDownloading(final int progress) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MoorLogUtils.d((int) (progress));
                                item.setFileProgress((int) (progress));
                                adapter.notifyItemChanged(position);
                            }
                        });
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onDownloadFailed() {

                }
            });

        } else {
            try {
                Intent intent = new Intent();
                File file = new File(url);
                if (MoorSdkVersionUtil.over24()) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri contentUri = FileProvider.getUriForFile(MoorUtils.getApp()
                            , MoorUtils.getApp().getPackageName() + MoorDemoConstants.MOOR_FILE_PROVIDER, file);
                    intent.setDataAndType(contentUri, MoorMimeTypesTools.getMimeType(MoorUtils.getApp(), item.getFileName()));
                } else {
                    intent.setDataAndType(Uri.fromFile(file), MoorMimeTypesTools.getMimeType(MoorUtils.getApp(), item.getFileName()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 点赞、点踩点击事件
     *
     * @param item
     * @param isUseFul
     */
    private void dealRobotFeedBack(final MoorMsgBean item, final boolean isUseFul) {
        if (isUseFul) {
            item.setRobotFeedBack(MoorDemoConstants.MOOR_ROBOT_USEFUL);
        } else {
            item.setRobotFeedBack(MoorDemoConstants.MOOR_ROBOT_UNUSEFUL);
        }

        updateListForRobotFeedBack(item);
        MoorMsgDao.getInstance().updateMoorMsgBeanToDao(item);

        MoorHttpUtils.post().params(MoorHttpParams.getInstance().robotFeedBack(
                item.getRobotId()
                , item.getOriQuestion()
                , item.getRobotType()
                , item.getStdQuestion()
                , item.getContent()
                , item.getConfidence()
                , isUseFul
                , item.getMessageId()))
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.ROBOT_FEEDBACK)
                .tag(getActivity())
                .build().execute(null);
    }

    /**
     * 点赞、点踩点击事件：更新列表数据
     *
     * @param item
     */
    private void updateListForRobotFeedBack(MoorMsgBean item) {
        //更新列表数据
        int index = 0;
        for (int i = msgList.size() - 1; i >= 0; i--) {
            if (msgList.get(i).getMessageId().equals(item.getMessageId())) {
                msgList.get(i).setRobotFeedBack(item.getRobotFeedBack());
                index = i;
                break;
            }
        }
        //刷新页面
        if (adapter != null) {
            adapter.notifyItemChanged(index, MoorDemoConstants.MOOR_PAYLOAD_USEFUL);
        }
    }

    /**
     * 文本联想数据
     */
    private void initAssociateData(final String key) {
        String botId = MoorManager.getInstance().getRobotId();
        String botType = MoorManager.getInstance().getRobotType();

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(botId) || TextUtils.isEmpty(botType)) {
            return;
        }
        //当前不是机器人状态，跳出
        if (!MoorConstants.NOW_ROBOT_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
            return;
        }

        //检查开关
        if (!MoorManager.getInstance().isAssociationInputSwitch()) {
            return;
        }

        MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.ROBOT_INPUTSUGGEST)
                .tag(getActivity())
                .params(MoorHttpParams.getInstance().inputSuggest(botId, botType, key))
                .build()
                .execute(new MoorBaseCallBack<MoorNetBaseBean<MoorInputSuggestBean>>() {
                    @Override
                    public void onSuccess(MoorNetBaseBean<MoorInputSuggestBean> result, int id) {
                        if (result.isSuccess()) {
                            if (result.getData() != null) {
                                ArrayList<String> datas = result.getData().getQuestions();
                                if (datas != null || datas.size() > 0) {
                                    rvEditAssociate.setVisibility(View.VISIBLE);
                                    if (datas.size() > 6) {
                                        ViewGroup.LayoutParams layoutParams = rvEditAssociate.getLayoutParams();
                                        layoutParams.height = MoorPixelUtil.dp2px(200);
                                        rvEditAssociate.setLayoutParams(layoutParams);
                                    }

                                    MoorChatAssociateAdapter tagLabeAdapter = new MoorChatAssociateAdapter(mActivity, key, datas);
                                    rvEditAssociate.setAdapter(tagLabeAdapter);

                                    tagLabeAdapter.setOnItemClickListener(new MoorChatAssociateAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(String flowBean) {
                                            sendTextMsg(flowBean);
                                            mEtChatInput.setText(null);
                                            rvEditAssociate.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    rvEditAssociate.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 打开评价框
     * 1：接口CHECK_CSRSTATUS 检查是否可以进行评价
     * 2：跳转
     *
     * @param msgId       消息Id，坐席主动发起评价时需要携带msgId
     * @param moorMsgBean 坐席主动发起评价后点击的弹出的itembean，完成评价需要更新状态
     * @param sessionId   收到会话结束自动弹出评价时带入 sessionid
     */
    private void openEvaluationDialog(String msgId, final MoorMsgBean moorMsgBean, String sessionId) {
        if (MoorAntiShakeUtils.getInstance().check()) {
            return;
        }
        if (TextUtils.isEmpty(msgId)) {
            msgId = "";
        }
        String session = "";
        if (moorMsgBean != null) {
            session = moorMsgBean.getSessionId();
        }

        if (TextUtils.isEmpty(session)) {
            session = sessionId;
        }

        final String finalMsgId = msgId;

        //检查是否可以进行评价
        final String finalSession = session;
        MoorHttpUtils
                .post()
                .tag(getActivity())
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.CHECK_CSR_STATUS)
                .params(MoorHttpParams.getInstance().checkCsrStatus(msgId, session))
                .build()
                .execute(new MoorBaseCallBack<MoorNetBaseBean<MoorCheckCsrStatusBean>>() {
                    @Override
                    public void onSuccess(final MoorNetBaseBean<MoorCheckCsrStatusBean> result, int id) {
                        if (MoorEvaluationContans.STATUS_CANEVALUATE.equals(result.getData().getStatus())) {
                            if (moorEvaluationBean != null) {
                                final MoorEvaluationDialog dialog = MoorEvaluationDialog.init(
                                        moorEvaluationBean,
                                        finalMsgId,
                                        result.getData().getSatisfyThank(),
                                        result.getData().getSatisfyTitle(), finalSession);
                                dialog.setEvaListener(new IMoorOnClickEvaListener() {
                                    @Override
                                    public void onClickSubmit() {
                                        if (moorMsgBean != null) {
                                            moorMsgBean.setSatisfactionInvite(true);
                                            MoorMsgDao.getInstance().updateMoorMsgBeanToDao(moorMsgBean);
                                            for (int i = msgList.size() - 1; i >= 0; i--) {
                                                if (msgList.get(i).getMessageId().equals(moorMsgBean.getMessageId())) {
                                                    msgList.get(i).setSatisfactionInvite(true);
                                                    if (adapter != null) {
                                                        adapter.notifyItemChanged(i);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        MoorMsgBean csrThankMsg = MoorMsgHelper.createCSRThankMsg(result.getData().getSatisfyThank());
                                        sendMsgToPage(csrThankMsg);
                                        dialog.close();
                                        if (!TextUtils.isEmpty(finalSession)) {
                                            MoorMsgDao.getInstance().changeCsrStatusToYes(finalSession);
                                            for (int i = msgList.size() - 1; i >= 0; i--) {
                                                if (finalSession.equals(msgList.get(i).getSessionId())) {
                                                    msgList.get(i).setSatisfactionInvite(true);
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onClickCancel() {
                                        dialog.close();
                                    }
                                });
//                                dialog.show(getFragmentManager(), "MoorEvaluationDialog");
                                if (getFragmentManager() != null) {
                                    getFragmentManager().beginTransaction().add(dialog, "MoorEvaluationDialog").commitAllowingStateLoss();
                                }
                            } else {
                                //评价信息数据为null
                            }

                        } else if (MoorEvaluationContans.STATUS_EVALUATIONED.equals(result.getData().getStatus())) {
                            MoorToastUtils.showShort(getResources().getString(R.string.moor_csr_status_evaluationed));
                        } else if (MoorEvaluationContans.STATUS_FAILURE.equals(result.getData().getStatus())) {
                            MoorToastUtils.showShort(getResources().getString(R.string.moor_csr_status_failure));
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MoorLogUtils.e(MoorUrlManager.CHECK_CSR_STATUS + ":" + e.getMessage());
                    }
                });

    }

    /**
     * 设置右上角按钮状态
     */
    private void setTitleRightView() {
        if (llChatClose != null && llChatConvert != null) {
            if (MoorConstants.NOW_ROBOT_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
                initManualButton();
                llChatClose.setVisibility(View.GONE);
            } else if (MoorConstants.NOW_CLAIM_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
                initCloseSessionButton();
                llChatConvert.setVisibility(View.GONE);
            } else {
                llChatConvert.setVisibility(View.GONE);
                llChatClose.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 转人工按钮
     * 显示转人工按钮，还需判断自定义配置 是否显示
     * 如果自定义配置(高优先级)没有添加转人工按钮，那么不显示转人工按钮
     */
    private void initManualButton() {
        if (!options.isTransferSeatsShow()) {
            llChatConvert.setVisibility(View.GONE);
            return;
        }
        llChatConvert.setVisibility(View.VISIBLE);
        llQueueLayout.setVisibility(View.GONE);
        boolean manualChat = MoorSPUtils.getInstance().getBoolean(MoorConstants.MOOR_MANUALBUTTON);//来自于会话中的转人工按钮显示
        if (manualChat) {
//                tv_chat_convert_close.setTextColor(optionsUI.getTransferSeatsBtnColor());
            String transferSeatsBtnImgUrl = options.getTransferSeatsBtnImgUrl();
            if (!TextUtils.isEmpty(transferSeatsBtnImgUrl)) {
                ivChatConvert.setVisibility(View.VISIBLE);
                MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + transferSeatsBtnImgUrl, ivChatConvert, MoorPixelUtil.dp2px(25f), MoorPixelUtil.dp2px(25f));
            } else {
                ivChatConvert.setVisibility(View.GONE);
            }

            String transferSeatsBtnText = options.getTransferSeatsBtnText();
            if (TextUtils.isEmpty(transferSeatsBtnText)) {
                tvChatConvert.setVisibility(View.GONE);
            } else {
                tvChatConvert.setVisibility(View.VISIBLE);
                tvChatConvert.setText(transferSeatsBtnText);
            }
        }

    }

    /**
     * 转人工按钮显示为 关闭会话按钮
     * 注意此处之后也收到自定义ui控制，远程设置不显示的话(优先级高)
     */
    private void initCloseSessionButton() {
        if (!options.isCloseSeatsSessionShow()) {
            llChatClose.setVisibility(View.GONE);
            return;
        }
        llChatClose.setVisibility(View.VISIBLE);
        llQueueLayout.setVisibility(View.GONE);
//            tv_chat_convert_close.setTextColor(optionsUI.getCloseSessionButtonColor());

        String transferSeatsBtnImgUrl = options.getCloseSeatsSessionBtnImgUrl();
        if (!TextUtils.isEmpty(transferSeatsBtnImgUrl)) {
            ivChatClose.setVisibility(View.VISIBLE);
            MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + transferSeatsBtnImgUrl, ivChatClose, MoorPixelUtil.dp2px(25f), MoorPixelUtil.dp2px(25f));
        } else {
            ivChatClose.setVisibility(View.GONE);
        }

        String transferSeatsBtnText = options.getCloseSeatsSessionBtnText();
        if (TextUtils.isEmpty(transferSeatsBtnText)) {
            tvChatClose.setVisibility(View.GONE);
        } else {
            tvChatClose.setVisibility(View.VISIBLE);
            tvChatClose.setText(transferSeatsBtnText);
        }
    }

    /**
     * 底部按钮数据添加
     */
    private void initBottomList(ArrayList<MoorBottomBtnBean> datas) {
        if (MoorConstants.NOW_ROBOT_STATUS.equals(MoorManager.getInstance().getChatStatus())) {
            llBottomLayout.setVisibility(View.VISIBLE);
            rvBottomtag.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

            MoorChatBottomLabelsAdapter tagLabeAdapter = new MoorChatBottomLabelsAdapter(datas);
            rvBottomtag.setAdapter(tagLabeAdapter);
            rvBottomtag.removeItemDecoration(decor);
            rvBottomtag.addItemDecoration(decor);
            tagLabeAdapter.setOnItemClickListener(new MoorChatBottomLabelsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MoorBottomBtnBean flowBean) {
                    if (flowBean.getButtonType() == 2) {
                        //跳转浏览器
                        MoorActivityHolder.getCurrentActivity().startActivity(MoorUtils.getStartWebActionIntent(flowBean.getText()));
                    } else {
                        sendTextMsg(flowBean.getText());
                    }
                }
            });
        }

    }

    /**
     * 转人工
     *
     * @param keyManual 转人工文案(转人工按钮null,机器人消息中配置的转人工数据)
     */
    private void convertManual(String keyManual) {
        String botId = MoorManager.getInstance().getRobotId();
        String botType = MoorManager.getInstance().getRobotType();
        if (TextUtils.isEmpty(botId) || TextUtils.isEmpty(botType)) {
            return;
        }
        loadingDialog.show();
        MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.ROBOT_CONVERTMANUAL)
                .tag(getActivity())
                .params(MoorHttpParams.getInstance().convertManual(botId, botType, keyManual))
                .build()
                .execute(new MoorBaseCallBack<MoorNetBaseBean<MoorInputSuggestBean>>() {
                    @Override
                    public void onSuccess(MoorNetBaseBean<MoorInputSuggestBean> result, int id) {
                        if (result.isSuccess()) {
                            loadingDialog.dismissDialog();
                            llChatConvert.setVisibility(View.GONE);
                        } else {
                            loadingDialog.dismissDialog();
                        }
                    }
                });
    }

    /**
     * 关闭会话
     */
    private void closeSession() {
        MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.FINISH_SESSION)
                .tag(getActivity())
                .params(MoorHttpParams.getInstance().finishSession())
                .build()
                .execute(new MoorBaseCallBack<MoorNetBaseBean<MoorInputSuggestBean>>() {
                    @Override
                    public void onSuccess(MoorNetBaseBean<MoorInputSuggestBean> result, int id) {
                        if (result.isSuccess()) {

                            MoorManager.getInstance().setChatStatus(MoorConstants.NOW_INIT_STATUS);
                            llChatClose.setVisibility(View.GONE);

                            MoorCloseSessionEvent closeSessionEvent = new MoorCloseSessionEvent();
                            MoorEventBusUtil.post(closeSessionEvent);

                            MoorManager.getInstance().clearChatStatus();
                            setMoreBtnVisiable();
                            updateUseFulState();
                        }
                    }
                });
    }

    /**
     * 关闭会话后，将sessionId置空，来隐藏列表中有无帮助的按钮
     */
    private void updateUseFulState() {
        infoBean.setSessionId("");
        MoorInfoDao.getInstance().createOrUpdate(infoBean);
        adapter.notifyItemRangeChanged(0, adapter.getItems().size() - 1, MoorDemoConstants.MOOR_PAYLOAD_HIDE);
    }

    /**
     * 点击分组常见问题的查看更多
     *
     * @param message
     */
    public void handleTabQuestionMoreClick(String title, ArrayList<String> message) {
        if (message != null) {
            if (message.size() > 0) {
                final MoorBottomTabQuestionDialog bottomTabQuestionDialog =
                        MoorBottomTabQuestionDialog.init(title, message);
                bottomTabQuestionDialog.show(getFragmentManager(), "");
                bottomTabQuestionDialog.setonQuestionClickListener(new MoorBottomTabQuestionDialog.OnQuestionClickListener() {
                    @Override
                    public void onItemClick(String s) {
                        sendTextMsg(s);
                        bottomTabQuestionDialog.close(true);
                    }
                });
            }
        }
    }

    /**
     * 点击物流节点 查看更多
     *
     * @param list
     * @param title
     * @param num
     */
    private void showMoreLogistics(ArrayList<MoorOrderInfoBean> list, String title, String
            num, String dialogTitle) {
        MoorBottomLogisticsProgressDialog dialog = MoorBottomLogisticsProgressDialog.init(
                title,
                num,
                list, dialogTitle);
        dialog.show(getFragmentManager(), "");
    }

    /**
     * 检查网络和弹出重连提示
     *
     * @return
     */
    private boolean checkNetDialog() {
        if (MoorManager.getInstance().checkNetOrWs()) {
            return true;
        } else {
            if (!wsErrorDialog.isShowing()) {
                wsErrorDialog.show(getFragmentManager(), "wsErrorDialog");
            }
            return false;
        }
    }

    private final MoorCommonBottomDialog.OnClickListener netErrorDialogClick = new MoorCommonBottomDialog.OnClickListener() {
        @Override
        public void onClickPositive() {
            if (MoorManager.getInstance().isHavaNetStatus()) {
                loadingDialog.show();
                MoorManager.getInstance().reConnectMoorServer();
                wsErrorDialog.dismiss();
            } else {
                MoorToastUtils.showShort(getResources().getString(R.string.moor_net_error));
                if (mActivity != null) {
                    wsErrorDialog.dismiss();
                    mActivity.finish();
                }
            }
        }

        @Override
        public void onClickNegative() {
            if (mActivity != null) {
                wsErrorDialog.dismiss();
                mActivity.finish();
            }
        }
    };

    /**
     * 点击订单卡片
     * String[0]：点击类型
     * String[1]：点击数据
     *
     * @param obj
     */
    private void clickOrderCard(Object[] obj) {
        if (obj.length == 2) {
            if (MoorOrderCardBean.TAGTYPE_USER.equals((String) obj[0])) {
                MoorToastUtils.showShort((String) obj[1]);
            } else if (MoorOrderCardBean.TAGTYPE_URL.equals((String) obj[0])) {
                if (!TextUtils.isEmpty((String) obj[1])) {
                    //跳转浏览器
                    MoorActivityHolder.getCurrentActivity().startActivity(MoorUtils.getStartWebActionIntent((String) obj[1]));
                }
            } else if (MoorOrderCardBean.TAGTYPE_SELF.equals((String) obj[0])) {
            } else {
            }
        }
    }

    /**
     * 发送订单卡片
     */
    private void sendOrderCard() {
        String order = MoorUtils.createOrderCardInfoMsg("商品订单", "订单号:", "0123456789012345",
                true, "确认订单", MoorOrderCardBean.TAGTYPE_URL, "https://www.baidu.com/?tn=64075107_1_dg",
                true, "取消订单", MoorOrderCardBean.TAGTYPE_SELF, "",
                "这是商品描述或者为订单描述文字说明说明说明", "￥9999", "https://dpic.tiankong.com/1n/e2/QJ6231550446.jpg@!350h", MoorOrderCardBean.TAGTYPE_USER, "TARGETTYPE_USER");
        if (!TextUtils.isEmpty(order)) {
            MoorMsgBean orderMsg = MoorMsgHelper.createOrderCardMsg(order);
            sendMsgToPage(orderMsg);
            jobManagerSendMsg(orderMsg);
        }
    }

    /**
     * 输入预知
     *
     * @param content
     */
    private void typeNotice(String content) {
        MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.TYPE_NOTICE)
                .tag(getActivity())
                .params(MoorHttpParams.getInstance().typeNotice(content))
                .build()
                .execute(new MoorBaseCallBack<MoorNetBaseBean>() {
                    @Override
                    public void onSuccess(MoorNetBaseBean result, int id) {
                    }
                });
    }

    /**
     * 消费消息
     */
    private void consumptionMsg() {
        if (isFront) {
            final ArrayList<MoorMsgBean> msgBeans = new ArrayList<MoorMsgBean>();
            final ArrayList<String> msgIds = new ArrayList<String>();
            for (MoorMsgBean bean : msgList) {
                if (bean.getFrom().equals(MoorChatMsgType.MSG_TYPE_RECEIVED)
                        && !bean.isDealUserMsg()) {
                    msgBeans.add(bean);
                    msgIds.add(bean.getMessageId());
                }
            }
            MoorHttpUtils.post()
                    .url(MoorUrlManager.BASE_URL + MoorUrlManager.DEAL_IMMSG)
                    .tag(getActivity())
                    .params(MoorHttpParams.getInstance().dealImMsg(msgIds))
                    .build()
                    .execute(new MoorBaseCallBack<MoorNetBaseBean>() {
                        @Override
                        public void onSuccess(MoorNetBaseBean result, int id) {
                            MoorLogUtils.d(result);
                            if (result.isSuccess()) {
                                MoorMsgDao.getInstance().updateIsDealUserMsg();
                                for (MoorMsgBean bean : msgBeans) {
                                    bean.setDealUserMsg(true);
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 申请解封
     */
    private void unblockBlackList() {
        MoorPanelDataHelper.getInstance().removeItem(MoorPanelBean.TYPE.TYPE_UNBLOCK);
        mChatPanelView.updatePanel(MoorPanelDataHelper.getInstance().getPanelBeanList());
        sendTextMsg(unblockContent);
    }


}
