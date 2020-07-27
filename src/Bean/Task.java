package Bean;

public class Task {


    private int id;//事件id
    private String name;//事件名称
    private String content;//事件内容
    private int start_id;//发起人id
    private String start_time;//发起时间
    private String preset_time;//预计时间
    private String execute_people;//执行人
    private String assist_people;//协助人
    private int agree_id;//批准人id
    private String agree_time;//批准时间
    private String finish_time;//完成时间
    private String state;//任务状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStart_id() {
        return start_id;
    }

    public void setStart_id(int start_id) {
        this.start_id = start_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPreset_time() {
        return preset_time;
    }

    public void setPreset_time(String preset_time) {
        this.preset_time = preset_time;
    }

    public String getExecute_people() {
        return execute_people;
    }

    public void setExecute_people(String execute_people) {
        this.execute_people = execute_people;
    }

    public String getAssist_people() {
        return assist_people;
    }

    public void setAssist_people(String assist_people) {
        this.assist_people = assist_people;
    }

    public int getAgree_id() {
        return agree_id;
    }

    public void setAgree_id(int agree_id) {
        this.agree_id = agree_id;
    }

    public String getAgree_time() {
        return agree_time;
    }

    public void setAgree_time(String agree_time) {
        this.agree_time = agree_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }





}
