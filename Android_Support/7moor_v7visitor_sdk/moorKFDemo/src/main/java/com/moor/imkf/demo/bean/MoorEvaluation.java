package com.moor.imkf.demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/5/15
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorEvaluation implements Serializable {


    public String _id;
    public String name;
    public String key;
    public List<MoorEvaluation> options;
    public boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoorEvaluation) {
            MoorEvaluation user = (MoorEvaluation) obj;
            if (user.name.equals(this.name) && user.key.equals(this.key)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }
}
