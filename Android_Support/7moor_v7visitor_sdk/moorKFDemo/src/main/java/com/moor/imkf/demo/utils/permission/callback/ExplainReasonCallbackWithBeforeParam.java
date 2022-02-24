package com.moor.imkf.demo.utils.permission.callback;


import com.moor.imkf.demo.utils.permission.request.ExplainScope;

import java.util.List;


public interface ExplainReasonCallbackWithBeforeParam {

    void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest);

}
