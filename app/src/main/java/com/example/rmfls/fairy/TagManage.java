package com.example.rmfls.fairy;

public class TagManage {
    String tag[] = new String[100];
    int count = 0;

    public TagManage() {

        push("토끼");
        push("거북");
    }

    public String compare(String note) {

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
        count = 0;
    }
}
