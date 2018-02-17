<%@page import="polynomialderivativesweb.Model"%>
<!DOCTYPE html>
<!--
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Polynomial Derivatives Calculator</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8">
        <meta name="viewport"> 
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body style="background-color:black">
        <h2> Polynomial Derivatives Calculator</h2>
        <h6> Polynomial degree value should be greater or equal 1 <br>
             Derivative order value should be greater or equal 1 <br>
             Number of coefficients entered shoud equal degree value entered + 1</h6> <br>
        
        <div class="parent">
            
        <div class="nowa">
            ------<font color="red"> GET method </font>------
        <form action="CalculationServlet" method="GET">
            Enter polynomial degree<br>
            <input type="text" size="1" name="degreeText">
            <br>
            Enter derivative order<br> 
            <input type="text" size="1" name="orderText">
            <br>
            Enter coefficients values<br>
            <input type="text" size="20" name="coefficientsText" > <br>
            <h1>(Begin from a0, seperate with space)</h1>
            <br>
            <input type="submit" value="Calculate!" />
        </form>
        
            
        </div>
        <div class="nowa2">
            ------ <font color="red">POST method </font>------
        <form action="CalculationServlet" method="POST">
            Enter polynomial degree<br>
            <input type="text" size="1" name="degreeText">
            <br>
            Enter derivative order<br> 
            <input type="text" size="1" name="orderText">
            <br>
            Enter coefficients values<br>
            <input type="text" size="20" name="coefficientsText" > <br>
            <h1>(Begin from a0, seperate with space)</h1>
            <br>
            <input type="submit" value="Calculate!" />
        </form>
        
        </div>
        </div>
        <div class="nowa3">
        <form action="HistoryServlet" >
            <input type="submit" value="Get history!">
        </form>
        </div>
    </body>
</html>
