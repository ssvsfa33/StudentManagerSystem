

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class text {

    private static final int ADD_Student=1;
    private static final int DEL_Student=2;
    private static final int UPDATE_Student=3;
    private static final int SEARCH_Student=4;
    private static final int Exit=5;
    private static final int LOGIN=1;
    private static final int SIGNUp=2;
    private static final int FORGETPASSWORD=3;
    private static final int LOGEXIT=4;


    public static void main(String[] args) {
        ArrayList<User> userlist=new ArrayList<>();
        ArrayList<Student> stulist=new ArrayList<>();
        Scanner sc=new Scanner(System.in);
        loginui(userlist, stulist);


    }

    //登录界面
    public static void loginui(ArrayList<User> userlist, ArrayList<Student> stulist){
        Scanner sc=new Scanner(System.in);
        int num=0;
        while(num!=4){
            System.out.println("欢迎来到jay的学生管理系统");
            System.out.println("请选择操作：1登录 2注册 3忘记密码 4退出");
            num=sc.nextInt();
            logchoose(userlist, num, stulist);
        }
    }

    //选择系统功能界面
    public static void chooseui(ArrayList<Student>list){
        Scanner sc=new Scanner(System.in);
        int num=0;
        while(num!=5){
            System.out.println("-------------欢迎来到Jay的学生管理系统-------------");
            System.out.println("1: 添加学生");
            System.out.println("2: 删除学生");
            System.out.println("3: 修改学生");
            System.out.println("4: 查询学生");
            System.out.println("5: 退出");
            System.out.println("请输入您的选择:");
            num=sc.nextInt();
            choose(list,num);
        }
    }

    //登录选择操作
    public static void logchoose(ArrayList<User> userlist, int num, ArrayList<Student> stulist){
        switch(num){
            case LOGIN:
                boolean flag=login(userlist);
                if(flag){
                    System.out.println("登录成功");
                    chooseui(stulist);
                    break;
                }else{
                    System.out.println("登录失败");
                    break;
                }
            case SIGNUp:
                User u=signup(userlist);
                if(u!=null){
                    userlist.add(u);
                    System.out.println("注册成功");
                    break;
                }else{
                    System.out.println("注册失败");
                    break;
                }
            case FORGETPASSWORD:
                User u1=changepassword(userlist);
                if(u1!=null){

                    System.out.println("密码修改成功");
                    break;
                }else{
                    System.out.println("密码修改失败");
                    break;
                }
            case LOGEXIT:
                break;
            default:
                System.out.println("您输入的选择有误，请重新输入");
                break;
        }
    }

    //选择系统功能操作
    public static void choose(ArrayList<Student> listn, int num){
        Scanner sc=new Scanner(System.in);

        switch(num){
            case ADD_Student:
                Student s=addstudent(listn);
                listn.add(s);
                break;
            case DEL_Student:
                System.out.println("请输入你要删除的学生的学号");
                String id=sc.next();
                delStudent(listn,id);
                break;
            case UPDATE_Student:
                System.out.println("请输入你要修改的学生的学号");
                id=sc.next();
                Student s1=Modify(listn,id);
                listn.add(s1);
                break;
            case SEARCH_Student:
                text(listn);
                break;
            case Exit:
                break;
            default:
                System.out.println("您输入的选择有误，请重新输入");
                break;
        }
    }

    //登录操作
    public static boolean login(ArrayList<User> userlist){
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username=sc.next();
        //检查用户名是否存在
        boolean flag=isuserif(userlist, username);
        //如果用户名存在
        //则检查密码是否正确
        if(flag){
            System.out.println("请输入密码:");
            String password=sc.next();
            String v=Verify();
            System.out.println("请输入验证码："+v);
            String verify=sc.next();
            for(int i=0;i<userlist.size();i++){
                int count=3-i;
                if(count <=0){
                    count=0;
                }
                User u=userlist.get(i);
                if(username.equals(u.getUsername())&&password.equals(u.getPassword())&&verify.equalsIgnoreCase(v)){
                    return true;
                }else if(!password.equals(u.getPassword())){
                    System.out.println("密码错误,请重新输入，剩余次数"+count+"次");
                    password=sc.next();
                    if(count==0){
                        System.out.println("密码错误,3次均失败");
                        break;
                    }
                }else if(!verify.equalsIgnoreCase(v)){
                    System.out.println("验证码错误,请重新输入");
                    verify=sc.next();
                }
            }
        }else{
            System.out.println("用户名不存在,请先注册");
        }
        return false;

    }

    //注册操作
    public static User signup(ArrayList<User> userlList){
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username=sc.next();
        System.out.println("请输入密码:");
        String password=sc.next();
        System.out.println("请输入身份证号:");
        String personid=sc.next();
        System.out.println("请输入手机号:");
        long phonenumber=sc.nextLong();
        //检查用户名是否存在
        boolean usernameflag=isuserif(userlList, username);
        //检查身份证号格式是否正确
        boolean personidflag=ispidright(personid);
        //检查手机号格式是否正确
        boolean phonenumberflag=isphonenumberright(phonenumber);
        //如果用户名不存在且身份证号格式正确且手机号格式正确
        //则注册成功
        if(!usernameflag&&personidflag&&phonenumberflag){
            User u=new User(username,password,personid,phonenumber);
            userlList.add(u);
            return u;
        }else if(!personidflag){
            System.out.println("身份证号码格式错误");
        }else if(!phonenumberflag){
            System.out.println("手机号格式错误");
        }else if(usernameflag){
            System.out.println("用户名已存在");
        }
        return null;
    }

    //修改密码
    public static User changepassword(ArrayList<User> userlList){
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username=sc.next();
        boolean usernameflag=isuserif(userlList, username);
        //如果用户名存在
        //则检查手机号是否正确
        if(usernameflag){
            for(int i=0;i<userlList.size();i++){
                System.out.println("请输入注册时使用的手机号");
                long phonenumber=sc.nextLong();
                User u=userlList.get(i);
                if(username.equals(u.getUsername())&&phonenumber==u.getPhonenumber()){
                    System.out.println("请输入新密码:");
                    String newpassword=sc.next();
                    u.setPassword(newpassword);
                    return u;
                }else{
                    System.out.println("手机号错误");
                }
            }
        }
        return null;
    }

    //检查身份证号格式是否正确
    public static boolean ispidright(String personid){
        //检查身份证号长度是否为18位
        if(personid.length()==18){
            //检查身份证号前17位是否只包含数字
            for(int i=0;i<personid.length()-1;i++){
                char c=personid.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //检查手机号格式是否正确
    public static boolean isphonenumberright(long phonenumber){
        //将手机号转换为字符串
        String phone=phonenumber+"";
        //检查手机号长度是否为11位
        if(phone.length()==11){
            //检查手机号是否以0开头
            int num=phone.charAt(0);
            if(num=='0'){
                return false;
            }
            //检查手机号是否只包含数字
            for(int i=0;i<phone.length();i++){
                char c=phone.charAt(i);
                if(c<'0'||c>'9'){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //检查用户名是否存在
    public static boolean isuserif(ArrayList<User> userlist, String username){
        if(userlist.size()==0){
            return false;
        }
        for(int i=0;i<userlist.size();i++){
            User u=userlist.get(i);
            if(username.equals(u.getUsername())){
                return true;
            }
        }
        return false;
    }

    //检查学号是否存在
    public static boolean examine(ArrayList<Student> listn, String id){
        if(listn.size()==0){
            return false;
        }
        for(int i=0;i<listn.size();i++){
            Student s=listn.get(i);
            if(s!=null){
                if(id.equals(s.getId())){
                    return true;
                }
            }
        }
        return false;
    }

    //查询是否存在该学生
    public static boolean search(ArrayList<Student> listn, String id){
        if(listn.size()==0){
            return false;
        }
        for(int i=0;i<listn.size();i++){
            Student s=listn.get(i);
            if(id.equals(s.getId())){
                return true;
            }
        }
        return false;
    }

    //添加学生
    public static Student addstudent(ArrayList<Student> listn){
        Scanner SC=new Scanner(System.in);
        Student s=new Student();
        System.out.println("请输入学生ID:");
        String id=SC.next();
        boolean flag=examine(listn,id);
        if(flag){
            System.out.println("该学号已存在");
            return null;
        }
        System.out.println("请输入学生姓名:");
        String name=SC.next();
        System.out.println("请输入学生年龄:");
        int age=SC.nextInt();
        System.out.println("请输入学生地址:");
        String address=SC.next();
        s.setId(id);
        s.setName(name);
        s.setAge(age);
        s.setAddress(address);
        System.out.println("添加成功");
        return s;
    }

    //删除学生
    public static void delStudent(ArrayList<Student> listn, String id){
        boolean flag=search(listn,id);
        if(!flag){
            System.out.println("该学号不存在");
            return;
        }
        for(int i=0;i<listn.size();i++){
            Student s=listn.get(i);
            if(id.equals(s.getId())){
                listn.remove(i);
                System.out.println("删除成功");
                break;
            }
        }
    }

    //修改学生
    public static Student Modify(ArrayList<Student> listn, String id){
        Scanner SC=new Scanner(System.in);
        boolean flag=search(listn, id);
        if(flag){
            delStudent(listn, id);
            Student s=addstudent(listn);
            return s;
        }else{
            System.out.println("该学号不存在");
            return null;
        }
    }

    public static void text(ArrayList<Student> listn){
        if(listn.size()==0){
            System.out.println("当前没有学生信息");
        }else{
            for(int i=0;i<listn.size();i++){
                Student s=listn.get(i);
                if(s!=null){
                    System.out.println(s.getId()+" "+s.getName()+" "+s.getAge()+" "+s.getAddress());
                }
            }
        }
    }

    //生成验证码
    public static String Verify(){
        Random r=new Random();
        String verify="";
        char []arr=new char[52];
        for(int i=0;i<arr.length/2;i++){
            arr[i]=(char)('a'+i);
            arr[i+26]=(char)('A'+i);
        }
        for(int i=0;i<4;i++){
            int index=r.nextInt(arr.length);
            verify+=arr[index];
        }
        int num=r.nextInt(10);
        verify+=num;

        char[] arr2=verify.toCharArray();
        int index=r.nextInt(verify.length()-1);
        char c=arr2[4];
        arr2[4]=arr2[index];
        arr2[index]=c;
        verify=new String(arr2);
        return verify;
    }
}
