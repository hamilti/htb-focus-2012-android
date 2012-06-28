package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.json.JSONObject;



public class FAQ {

    public final int faqId;
    public final String question, answer;


    FAQ(JSONObject o) {
        this.faqId = o.optInt("id");
        this.question = JSON.getString(o, "question");
        this.answer = JSON.getString(o, "answer");
    }

}
