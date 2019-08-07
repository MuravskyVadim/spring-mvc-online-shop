<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Welcome to shop!!!</title>
  </head>
  <body>
  <div style="text-align: center;">
    <p>Welcome! You can register if you want!</p>
    <form action="/login" method="POST">
      <p>E-mail:<br><input type="email" name="email" value="${email}"></p>
      <p>Password:<br><input type="password" name="password"></p>
      ${message}
      <p><button type="submit">Login</button></p>
    </form>
  </div>
  </body>
</html>
