package com.example.rmfls.speechtest;

import android.widget.Toast;

import java.io.Serializable;

public class TagManage implements Serializable {
    static String fariy = ""; //어떤 동화 골랐는지 저장
    static String tag[] = new String[100];//태그를 저장
    static int count = 0;//태그 인덱스

    public TagManage() {

    }

    public String Compare(String note) {

        for (int i = 0; i < tag.length; i++) {// 태그들을 비교
            if (tag[i] == null)//태그가 비지 않았나
                return "false";

            else {
                if (tag[i].length() <= note.length())//태그와 노트 크기 비교 있는지도 비교
                    for (int j = 0; j != tag[i].length(); j++) {//태그와 노트 글자비교
                        if (tag[i].substring(j, j + 1).equals(note.substring(j, j + 1)) == true)//글자 하나하나 비교
                            if (j + 1 == tag[i].length())
                                return tag[i];
                            else {
                            }
                    }
            }
        }
        return "false";
    }

    void push(String tagNote) {
        tag[count++] = tagNote;
    }

    void clean() {
        tag = new String[100];
        count = 0;
    }

    public int cview() {
        int cc = count;
        return cc;
    }
}
