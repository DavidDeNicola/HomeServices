<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Footer</title>
<style>
    .footer-container {
        display: flex;
        align-items: flex-start;
        position: relative; 
        padding: 30px 0;
        max-width: 1200px;
        margin: 0 auto;
    }

    .footer-left, .footer-right {
        flex: 1; 
        display: flex;
        justify-content: center; 
    }


    .footer-right-content {
        display: flex;
        gap: 60px; 
    }


    .central-divider {
        width: 1px;
        background-color: #ccc;
        
        align-self: stretch; 
        margin: 0;
    }

    ul {
        list-style-type: none;
        padding: 0;
        margin: 0;
    }

    .footer-bottom {
        text-align: center;
        margin-top: 20px;
        padding: 15px 0;
        border-top: 1px solid #eee;
    }

    @media (max-width: 768px) {
        .footer-container {
            flex-direction: column;
            align-items: center;
            gap: 30px;
        }
        .central-divider {
            width: 50%;
            height: 1px;
            align-self: center;
        }
        .footer-right-content {
            flex-direction: column;
            align-items: center;
            gap: 20px;
            text-align: center;
        }
        .footer-left { text-align: center; }
    }
</style>
</head>
<body>
	
	<footer class="sfondo2">
    	<div class="footer-container">
            
            <div class="footer-left">
                <div class="footer-column">
                	<h4>HomeServices</h4>
                	<p class="small mb-0">Connettiamo professionisti qualificati<br> a chi ha bisogno di aiuto.</p>
                </div>
        	</div>

            <div class="central-divider"></div>
            
            <div class="footer-right">
                <div class="footer-right-content">
                    <div class="footer-column">
                        <h4>Scopri</h4>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/servizi">Tutti i servizi</a></li>
                            <li><a href="${pageContext.request.contextPath}/chi-siamo">Chi siamo</a></li>
							<li><a href="${pageContext.request.contextPath}/FAQ">FAQ</a></li>
                        </ul>
                    </div>
                    
                    <div class="footer-column">
                        <h4>Contatti:</h4>
                        <ul>
                            <li><a href="mailto:homeservices@gmail.com?subject=Richiesta informazioni">Email: homeservices@gmail.com</a></li>
                            <li><a href="tel:+393920626721">Telefono: 392 062 6721</a></li>
                        </ul>
                    </div>
                </div>
        	</div>
            
    	</div>
        
    	<div class="footer-bottom">
       		<p>© 2026 HomeServices</p>
    	</div>
	</footer>
	
</body>
</html>