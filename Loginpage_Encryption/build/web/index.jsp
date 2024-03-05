<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link href="indexStyles.css" rel="stylesheet" type="text/css"/>
    </head>
    <style>
body{
    display:flex;
    background-image: linear-gradient(to left top, #a88ae8, #ff76c1, #ff8173, #ffb60f, #a8eb12);
    justify-content:center;
    align-items:center;
    height:100vh;
    width:100%;
}

header {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    color: white;
    text-align: center;
    padding: 10px;
}

footer {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    color: white;
    text-align: center;
    padding: 10px;
}

.login-container{
    width:420px;
    background:transparent;
    border:2px double rgba(255,255,255,0.2);
    box-shadow:0 0 10px rgba(0,0,0,0.2);
    border-radius:10px;
    color:#fff;
    padding:50px 50px;
}

.login-container h1{
     font-size:36px;
     text-align:center;
}

.login-container .input-box{
    width:100%;
    height:50px;
    margin:30px 0;
}

.input-box input {
    width:100%;
    height:100%;
    background:transparent;
    border:none;
    outline:none;
    border: 2px solid rgba(255, 255, 255, 0.2);
    border-radius:40px;
    font-size:16px;
    color:#fff;
    padding:10px;
    box-sizing: border-box;
    transition: 0.3s;
}
.input-box input:hover{
    border:2px solid red;
}

.input-box input::placeholder{
    color:#fff;
}

.input-box input:focus::placeholder{
    color:red;
}

.btn{
    width:100%;
    height:45px;
    background:#fff;
    border:none;
    outline:none;
    border-radius:40px;
    box-shadow:0 0 10px rgba(0,0,0,0.1);
    cursor:pointer;
    font-size:16px;
    color:#333;
    transition:0.3s;
}

.btn:hover {
  background-color: red;
  color: white;
}        
    </style>
    <body>
        <%
            response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); //HTTP 1.1
            response.setHeader("Pragma","no-cache"); //HTTP 1.0
            response.setHeader("Expires","0"); //Proxies
            
            String prevUrl = (String) session.getAttribute("previousUrl");
            session.removeAttribute("username"); // Only new code added
//            session.removeAttribute("previousUrl");        
            
            session.setAttribute("isLogin",false);
            
            if(prevUrl != null){
                session.removeAttribute("previousUrl");
                response.sendRedirect(prevUrl);
            }
        %>
        <div class="login-container">
            
            <form action="LoginServlet" method="POST">
                <h1>Login</h1>
                <div class="login-credentials">
                <div class="input-box">
                    <input type="text" name="username" placeholder="Username">
                </div>
                <div class="input-box">
                    <input type="password" name="password" placeholder="Password">
                </div>
                </div>

            <a href="/success.jsp"><button type="submit" class="btn">Login</button></a>
            </form>
        </div>
    </body>
</html>