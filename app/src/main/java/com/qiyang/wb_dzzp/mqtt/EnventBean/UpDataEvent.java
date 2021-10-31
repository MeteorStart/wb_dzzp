package com.qiyang.wb_dzzp.mqtt.EnventBean;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/8/15 9:45
 * @company:
 * @email: lx802315@163.com
 */
public class UpDataEvent {

    /**
     * type : notice_
     * data : {"grade":"1","version":"","title":"","content":"","url":""}
     * time : 2021-10-30 17:57:22
     */

    private String type;
    private DataDTO data;
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataDTO {
        /**
         * grade : 1
         * version :
         * title :
         * content :
         * url :
         */

        private String grade;
        private String version;
        private String title;
        private String content;
        private String url;

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
