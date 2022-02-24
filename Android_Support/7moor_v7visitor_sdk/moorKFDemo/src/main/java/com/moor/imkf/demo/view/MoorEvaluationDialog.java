package com.moor.imkf.demo.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorTagSelectAdapter;
import com.moor.imkf.demo.bean.MoorEvaluation;
import com.moor.imkf.demo.bean.MoorEvaluationBean;
import com.moor.imkf.demo.listener.IMoorOnClickEvaListener;
import com.moor.imkf.demo.utils.MoorAntiShakeUtils;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorhttp.MoorHttpParams;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorhttp.callback.MoorBaseCallBack;
import com.moor.imkf.moorsdk.bean.MoorCheckCsrStatusBean;
import com.moor.imkf.moorsdk.bean.MoorNetBaseBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.toast.MoorToastUtils;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/5/14
 *     @desc   : 评价弹框
 *     @version: 1.0
 * </pre>
 */
@SuppressLint("ValidFragment")
public class MoorEvaluationDialog extends BottomSheetDialogFragment {

    protected Context mContext;
    protected View rootView;
    protected BottomSheetDialog dialog;
    protected BottomSheetBehavior mBehavior;
    private ImageView ivBottomClose;
    private MoorMeasureListView lvEva;
    private MoorEvaScrollView moorEvaScroll;
    private MoorTagView tagView;
    private EditText etEva;
    private TextView tvEvaEditNum, tv_eva_title;
    private MoorShadowLayout slSubmit;
    private MoorShadowLayout slCancel;
    private IMoorOnClickEvaListener evaListener;
    private MoorEvaluationBean moorEvaluationBean;//评价信息数据
    private String satisfyTitle;//满意度评价提示语
    private String satisfyThank;//提交满意度评价后感谢语
    private ArrayList<MoorEvaluation> selectLabels = new ArrayList<MoorEvaluation>();//点选的标签数据
    private String satisfactionName;//当前选择的 满意的类型
    private String satisfactionKey;//当前选择的 满意的类型key
    private String msgId;//消息Id，坐席推送的评价消息中点击进来会携带msgId
    private boolean isProposalStatus = false;//备注是否必填，默认不必填
    private EvelautionAdapter adapter;
    private MoorOptions optionsUI;
    private LayerDrawable image;
    private String session;

    public static MoorEvaluationDialog init(MoorEvaluationBean data, String msgId, String satisfyTitle, String satisfyThank, String session) {

        MoorEvaluationDialog dialog = new MoorEvaluationDialog();
        Bundle args = new Bundle();
        args.putSerializable("moorEvaluationBean", data);
        args.putString("msgId", msgId);
        args.putString("satisfyThank", satisfyThank);
        args.putString("satisfyTitle", satisfyTitle);
        args.putString("session", session);
        dialog.setArguments(args);
        return dialog;

    }

    public MoorEvaluationDialog() {

    }

    public void setEvaListener(IMoorOnClickEvaListener evaListener) {
        this.evaListener = evaListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        } else {
            dialog = new BottomSheetDialog(getContext(), R.style.moor_BottomSheetStyle);
        }

        Bundle arguments = getArguments();
        assert arguments != null;
        moorEvaluationBean = (MoorEvaluationBean) arguments.getSerializable("moorEvaluationBean");
        msgId = arguments.getString("msgId");
        satisfyThank = arguments.getString("satisfyThank");
        satisfyTitle = arguments.getString("satisfyTitle");
        session = arguments.getString("session");


        optionsUI = (MoorOptions) MoorManager.getInstance().getOptions();
        if (optionsUI == null) {
            optionsUI = new MoorOptions();
        }
        if (rootView == null) {
            rootView = View.inflate(mContext, R.layout.moor_evaluation_dialog, null);
            ivBottomClose = rootView.findViewById(R.id.iv_bottom_close);
            lvEva = rootView.findViewById(R.id.lv_eva);
            moorEvaScroll = rootView.findViewById(R.id.moor_eva_scroll);
            tagView = rootView.findViewById(R.id.tag_eva);
            etEva = rootView.findViewById(R.id.et_eva);
            tvEvaEditNum = rootView.findViewById(R.id.tv_eva_edit_num);
            slSubmit = rootView.findViewById(R.id.sl_submit);
            slCancel = rootView.findViewById(R.id.sl_cancel);
            tv_eva_title = rootView.findViewById(R.id.tv_eva_title);
        }

        initOptionsUI();

        setDialog();
        initData();
        addListener();
        etEva.addTextChangedListener(textChanged);

        return dialog;
    }

    /**
     * UI配置
     */
    private void initOptionsUI() {
        image = createDraw();
        slSubmit.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, optionsUI.getSdkMainThemeColor()));
        slSubmit.setLayoutBackgroundTrue(MoorColorUtils.getColorWithAlpha(0.5f, optionsUI.getSdkMainThemeColor()));
    }


    private void initData() {
        if (TextUtils.isEmpty(satisfyTitle)) {
            tv_eva_title.setVisibility(View.GONE);
        } else {
            tv_eva_title.setText(satisfyTitle);
        }
        if (moorEvaluationBean.getRadioTagText() != null && moorEvaluationBean.getRadioTagText().size() > 0) {
            //添加评价满意度类型
            initRadioTag();
        }
    }

    private void initRadioTag() {
        ArrayList<MoorEvaluationBean.RadioTagTextBean> radioTagTextBeans = moorEvaluationBean.getRadioTagText();
        ArrayList<MoorEvaluationBean.RadioTagTextBean> beans = new ArrayList<MoorEvaluationBean.RadioTagTextBean>();
        int default_select = radioTagTextBeans.get(0).getDefaultIndex();

        for (int i = 0; i < radioTagTextBeans.size(); i++) {
            if (radioTagTextBeans.get(i).getUse()) {
                MoorEvaluationBean.RadioTagTextBean investigate = radioTagTextBeans.get(i);
                if (default_select - 1 == i) {
                    radioTagTextBeans.get(i).setSlected(true);
                    //默认选中
                    if (default_select - 1 == i) {
                        satisfactionName = investigate.getName();
                        satisfactionKey = investigate.getKey();
                        selectLabels.clear();
                        List<MoorEvaluation> options = new ArrayList<>();
                        for (String reason : investigate.getReason()) {
                            MoorEvaluation option = new MoorEvaluation();
                            option.name = reason;
                            options.add(option);
                        }
                        tagView.initTagView(options, 1, new MoorTagSelectAdapter.OnSelectedChangeListener() {
                            @Override
                            public void getSelectedTagList(List<MoorEvaluation> evaluationList) {
                                if (evaluationList != null) {
                                    selectLabels.clear();
                                    selectLabels.addAll(evaluationList);
                                }
                            }
                        });

                        setProposalStatus(investigate.getProposalStatus());

                    }
                } else {
                    radioTagTextBeans.get(i).setSlected(false);
                }
                beans.add(radioTagTextBeans.get(i));
            }
        }


        adapter = new EvelautionAdapter(beans);
        lvEva.setAdapter(adapter);
    }

    private final TextWatcher textChanged = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int editStart = etEva.getSelectionStart();
            int editEnd = etEva.getSelectionEnd();
            etEva.removeTextChangedListener(textChanged);
            if (!TextUtils.isEmpty(etEva.getText())) {
                int maxLen = 50;
                while (s.toString().length() > maxLen) {
                    s.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                    MoorToastUtils.showShort(getResources().getText(R.string.moor_tips_textsolong));
                }
                tvEvaEditNum.setText(s.toString().length() + "/50");
            }

            etEva.setText(s);
            etEva.setSelection(editStart);

            // 恢复监听器
            etEva.addTextChangedListener(textChanged);
        }
    };

    private void setDialog() {
        //设置点击dialog外部不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(rootView);
        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setHideable(false);
//        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int newState) {
//                //禁止拖拽
//                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                    //设置为收缩状态
//                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });

        View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackgroundColor(mContext.getResources().getColor(R.color.moor_color_Translucent));

//        //重置高度
//        if (dialog != null) {
//            bottomSheet.getLayoutParams().height = MoorScreenUtils.getFullActivityHeight(mContext) * 4 / 5;
//        }
        rootView.post(new Runnable() {
            @Override
            public void run() {
                mBehavior.setPeekHeight(rootView.getHeight());
            }
        });
    }

    private void addListener() {
        ivBottomClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (evaListener != null) {
                    evaListener.onClickCancel();
                }
            }
        });
        slCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (evaListener != null) {
                    evaListener.onClickCancel();
                }
            }
        });

        /**
         * 提交按钮
         */
        slSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoorAntiShakeUtils.getInstance().check()) {
                    return;
                }
                //满意度类型必选
                if (TextUtils.isEmpty(satisfactionKey) | TextUtils.isEmpty(satisfactionName)) {
                    MoorToastUtils.showShort(getResources().getString(R.string.moor_chat_please_choosecsrtag));
                    return;
                }

                //备注必填
                if (isProposalStatus) {
                    if (TextUtils.isEmpty(etEva.getText().toString())) {
                        MoorToastUtils.showShort(getResources().getString(R.string.moor_csr_isProposalStatus));
                        return;
                    }
                }

                ArrayList<String> labls = new ArrayList<String>();
                for (MoorEvaluation s : selectLabels) {
                    labls.add(s.name);
                }
                MoorHttpUtils
                        .post()
                        .tag("")
                        .url(MoorUrlManager.BASE_URL + MoorUrlManager.SUBMIT_SENDCSRMSG)
                        .params(MoorHttpParams.getInstance().evaluationSubmit(
                                msgId,
                                moorEvaluationBean.getType(),
                                satisfactionKey, satisfactionName, etEva.getText().toString(), session, labls))
                        .build()
                        .execute(new MoorBaseCallBack<MoorNetBaseBean<MoorCheckCsrStatusBean>>() {
                            @Override
                            public void onSuccess(MoorNetBaseBean<MoorCheckCsrStatusBean> result, int id) {
                                if (evaListener != null) {
                                    evaListener.onClickSubmit();
                                }
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                if (evaListener != null) {
                                    evaListener.onClickSubmit();
                                }
                            }
                        });


            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
    }


    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    public void close() {
        dismiss();
    }

    class EvelautionAdapter extends BaseAdapter {
        private ArrayList<MoorEvaluationBean.RadioTagTextBean> beans;


        public EvelautionAdapter(ArrayList<MoorEvaluationBean.RadioTagTextBean> radioTagTextBeans) {
            this.beans = radioTagTextBeans;
        }

        @Override
        public int getCount() {
            return beans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MoorEvaluationBean.RadioTagTextBean investigate = beans.get(position);
            View view = View.inflate(getContext(), R.layout.moor_item_evaulation, null);
            MoorRoundImageView img = view.findViewById(R.id.iv_evelation_img);
            TextView text = view.findViewById(R.id.tv_evelation_text);
            text.setText(investigate.getName());
            img.setType(MoorRoundImageView.TYPE_CIRCLE);


            if (investigate.isSlected()) {
                img.setImageDrawable(image);
            } else {
                img.setImageResource(R.drawable.moor_icon_radio_unselect);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击类型切换满意度标签数据
                    for (int i = 0; i < beans.size(); i++) {
                        if (position == i) {
                            beans.get(i).setSlected(true);
                        } else {
                            beans.get(i).setSlected(false);
                        }
                    }
                    satisfactionName = investigate.getName();
                    satisfactionKey = investigate.getKey();
                    selectLabels.clear();
                    List<MoorEvaluation> options = new ArrayList<>();
                    for (String reason : investigate.getReason()) {
                        MoorEvaluation option = new MoorEvaluation();
                        option.name = reason;
                        options.add(option);
                    }

                    tagView.initTagView(options, 1, new MoorTagSelectAdapter.OnSelectedChangeListener() {
                        @Override
                        public void getSelectedTagList(List<MoorEvaluation> evaluationList) {
                            if (evaluationList != null) {
                                selectLabels.clear();
                                selectLabels.addAll(evaluationList);
                            }

                        }
                    });

                    setProposalStatus(investigate.getProposalStatus());

                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }


    private LayerDrawable createDraw() {
        Bitmap bitmap1 = Bitmap.createBitmap(MoorPixelUtil.dp2px(26), MoorPixelUtil.dp2px(26),
                Bitmap.Config.ARGB_8888);

        bitmap1.eraseColor(MoorColorUtils.getColorWithAlpha(1f, optionsUI.getSdkMainThemeColor()));

        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.moor_icon_radio_selected)).getBitmap();
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(bitmap1);
        array[1] = new BitmapDrawable(bitmap2);
        LayerDrawable la = new LayerDrawable(array);
        la.setLayerInset(0, 0, 0, 0, 0);
        return la;
    }


    /**
     * 评价备注是否必填和隐藏
     *
     * @param proposalStatus
     */
    private void setProposalStatus(String proposalStatus) {
        if ("required".equals(proposalStatus)) {
            isProposalStatus = true;
            etEva.setVisibility(View.VISIBLE);
            tvEvaEditNum.setVisibility(View.VISIBLE);
        } else if ("unRequired".equals(proposalStatus)) {
            isProposalStatus = false;
            etEva.setVisibility(View.VISIBLE);
            tvEvaEditNum.setVisibility(View.VISIBLE);
        } else if ("notShow".equals(proposalStatus)) {
            isProposalStatus = false;
            etEva.setVisibility(View.GONE);
            tvEvaEditNum.setVisibility(View.GONE);
        }
    }

}

