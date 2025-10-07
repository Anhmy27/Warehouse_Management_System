<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <meta name="description" content="POS - Bootstrap Admin Template">
        <meta name="keywords" content="admin, estimates, bootstrap, business, corporate, creative, invoice, html5, responsive, Projects">
        <meta name="author" content="Dreamguys - Bootstrap Admin Template">
        <meta name="robots" content="noindex, nofollow">
        <title>Login - Pos admin template</title>
        <link rel="shortcut icon" type="image/x-icon" href="assets/img/favicon.jpg">
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <style>
            .form-login.remember-me {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 20px;
            }

            .remember-checkbox {
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .remember-checkbox input[type="checkbox"] {
                width: 16px;
                height: 16px;
                cursor: pointer;
                margin: 0;
            }

            .remember-checkbox label {
                margin: 0;
                cursor: pointer;
                user-select: none;
                font-size: 13px;
                color: #666;
                font-weight: normal;
            }
        </style>
    </head>
    <body class="account-page">
        <div class="main-wrapper">
            <div class="account-content">
                <div class="login-wrapper">
                    <div class="login-content">
                        <div class="login-userset">
                            <div class="login-logo">
                                <img src="assets/img/logo.png" alt="img">
                            </div>
                            <%
    String email = "";
    String password = "";
    String rememberChecked = "";

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("email".equals(c.getName())) {
                email = c.getValue();
            }
            if ("password".equals(c.getName())) {
                password = c.getValue();
            }
            if ("rememberMe".equals(c.getName()) && "true".equals(c.getValue())) {
                rememberChecked = "checked";
            }
        }
    }
                            %>
                            <div class="login-userheading">
                                <h3>Sign In</h3>
                                <h4>Please login to your account</h4>
                            </div>
                            <form action="login" method="post">
                                <p style="color:red">${error}</p>

                                <div class="form-login">
                                    <label>Email</label>
                                    <div class="form-addons">
                                        <input type="text" name="email" value="<%= email %>"
                                               placeholder="Enter your email address" required>
                                        <img src="assets/img/icons/mail.svg" alt="img">
                                    </div>
                                </div>
                                <div class="form-login">
                                    <label>Password</label>
                                    <div class="pass-group">
                                        <input type="password" name="password" value="<%= password %>"
                                               class="pass-input" placeholder="Enter your password" required>
                                        <span class="fas toggle-password fa-eye-slash"></span>
                                    </div>
                                </div>
                                <div class="form-login remember-me">
                                    <div class="remember-checkbox">
                                        <input type="checkbox" id="rememberMe" name="rememberMe" value="true" <%= rememberChecked %>>
                                        <label for="rememberMe">Remember me</label>
                                    </div>
                                    <div class="alreadyuser">
                                        <h4><a href="forgetpassword.html" class="hover-a">Forgot Password?</a></h4>
                                    </div>
                                </div>
                                <div class="form-login">
                                    <button type="submit" class="btn btn-login">Sign In</button>
                                </div>
                            </form>
                            <div class="signinform text-center">
                                <h4>Don't have an account? <a href="signup.html" class="hover-a">Sign Up</a></h4>
                            </div>
                            <div class="form-setlogin">
                                <h4>Or sign up with</h4>
                            </div>
                        </div>
                    </div>
                    <div class="login-img">
                        <img src="assets/img/login.jpg" alt="img">
                    </div>
                </div>
            </div>
        </div>
        <script src="assets/js/jquery-3.6.0.min.js"></script>
        <script src="assets/js/feather.min.js"></script>
        <script src="assets/js/bootstrap.bundle.min.js"></script>
        <script src="assets/js/script.js"></script>
    </body>
</html>