# 学生管理系统（MVC 重构版）

对原 `text.java`（408 行全挤在一个类里）的 MVC 重构。
**功能和界面文案 100% 保留，只治病不动脸**：全程没有 `try/catch`、没有异常，
成败一律用**返回值**判断（`boolean` 成败、`String` 返回 `null` 表示成功），
只用到 if-else、switch、for、while 这些基础语法。

## MVC 结构

```
src/com/jay/studentmanager/
├── Main.java                入口：按 M → C → V 组装，启动登录菜单
├── model/                   【M】数据
│   ├── Student.java         学生：学号 姓名 年龄 地址
│   └── User.java            用户：用户名 密码 身份证 手机号(String)
├── service/                 【M】业务逻辑 + 内存数据
│   ├── StudentService.java  增删改查，boolean 返回成败
│   └── UserService.java     注册/登录/改密，String 返回错误原因（null=成功）
├── controller/              【C】界面和业务之间的转发
│   ├── AuthController.java  登录结果用 int 常量（LOGIN_SUCCESS=0 等）
│   └── StudentController.java 组装学生对象交给服务
├── view/                    【V】只负责提问和显示
│   ├── ConsoleUi.java       全程序唯一的 Scanner；输错数字不再闪退
│   ├── LoginView.java       登录 / 注册 / 忘记密码菜单
│   └── StudentView.java     学生增删改查菜单
└── util/                    零件
    ├── Validators.java      身份证 / 手机号格式校验
    └── CaptchaGenerator.java 验证码生成（4字母+1数字）

test/com/jay/studentmanager/
└── SmokeTest.java           业务测试 44 项（纯基础语法，无 IO）
```

## 怎么运行

| 方式 | 操作 |
|---|---|
| IDEA（推荐） | 打开本项目，直接运行 `com.jay.studentmanager.Main`（项目编码 UTF-8） |
| Windows 命令行 | 双击 `run.bat`（自动编译 + 启动，控制台已切 UTF-8） |
| 跑测试 | 双击 `test.bat`（44 项断言，全绿即通过） |

## 修复的原版 Bug

1. **注册双写**：原 `signup()` 内部 add 一次、调用方又 add 一次 → 每个用户存两份
2. **null 进列表**：添加/修改失败返回 null，`choose()` 照样 `list.add()` → 下次查询/删除 NPE 崩溃
3. **登录次数算错**：原用“用户列表下标”当“剩余次数”，只有 1 个用户时永远提示剩 3 次
4. **忘记密码循环错**：逐个用户问手机号，输错一次会问“下一个用户”的手机号
5. **修改=先删后加**：中途任何失败数据就丢，还会让你重输一遍学号
6. **8 个 Scanner**：8 处各 `new Scanner(System.in)` → 合并为 `ConsoleUi` 里唯一一个
7. **菜单输字母闪退**：`sc.nextInt()` 遇到非数字直接异常 → 现在提示后重新输入
8. **重复方法**：`examine()` 和 `search()` 一字不差写了两遍 → 合并

## 与原版仅有的 3 处行为微调

- 修改学生：不再重输学号（就地更新），成功提示改为“修改成功”（原版会先打印“删除成功”）
- 登录：货真价实的 3 次机会，提示的“剩余次数”终于对了
- 菜单输非数字：提示“输入有误，请输入数字”后重输（原版直接崩溃）

## 校验规则（与原版一致）

- 身份证号：18 位，前 17 位必须是数字（末位可为 X）
- 手机号：11 位数字，不以 0 开头
- 注册校验顺序：身份证 → 手机号 → 用户名是否已存在
- 学号：必填且唯一；年龄、地址必填
