package com.hobbyzhub.javabackend.mailingmodule.definition;

import java.time.LocalDateTime;

public abstract class MailDefinition {
    protected final String generateMailTemplate(String message) {
        // TODO: add link to the hobbyzhub homepage
        // TODO: add correct email for the hobbyzhub project
        // TODO: add link to the hobbyzhub logo on s3
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional //EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html>
                	<head>
                		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                		<meta name="viewport" content="width=320, target-densitydpi=device-dpi" />
                                
                		<style type="text/css">
                			@import url(http://fonts.googleapis.com/css?family=Roboto:400,300);
                			* {
                				margin: 0;
                				padding: 0;
                				font-family: Roboto, sans-serif;
                				box-sizing: border-box;
                				font-size: 14px;
                			}
                			body {
                				-webkit-font-smoothing: antialiased;
                				-webkit-text-size-adjust: none;
                				width: 100% !important;
                				height: 100%;
                				line-height: 1.6;
                			}
                			table td {
                				vertical-align: top;
                			}
                			@media only screen and (max-width: 640px) {
                				.container {
                					width: 100% !important;
                				}
                				.content,
                				.content-wrap {
                					padding: 10px !important;
                				}
                			}
                		</style>
                	</head>
                	<body>
                		<table style="background-color: #fff; width: 100%;">
                			<tr>
                				<td style="display: block !important; max-width: 640px !important; margin: 0 auto !important; clear: both !important;" width="640">
                					<div style="max-width: 640px; margin: 0 auto; display: block; padding: 16px;">
                						<div style="width: 100%; margin-top: 20px; margin-bottom: 20px;">
                							<table width="100%">
                							  <tr>
                								<td style="text-align: center; padding: 0 0 20px;">
                								  <a href="#">
                									<img src="https://files.fm/thumb.php?i=8ssg99wsek" />
                								  </a>
                								</td>
                							  </tr>
                							</table>
                						</div>
                						<table style="background: #fff; border: 1px solid #dddde0; border-radius: 3px; color: #444447;" width="100%" cellpadding="0" cellspacing="0">
                							<tr>
                								<td style="padding: 35px 35px 10px;">
                									<table width="100%" cellpadding="0" cellspacing="0">
                										<tr>
                											<td style="padding: 0 0 20px;">"""
                                                                + message + """
                											</td>
                										</tr>
                										<tr>
                											<td style="padding: 0 0 20px; padding-bottom: 0; font-size: 11px; line-height: 18px; color: #88888b;">
                												This is an automated e-mail message, please do not reply directly. You can
                												contact us by sending an e-mail to
                												<a href="mailto:#" target="_blank" style="color: #88888b; text-decoration: underline; font-size: 11px;"
                													>{{ mail_info }}</a
                												>.
                											</td>
                										</tr>
                									</table>
                								</td>
                							</tr>
                						</table>
                						<div style="width: 100%; clear: both; color: #88888b; padding: 15px 0 0 0;">
                							<table width="100%">
                								<tr>
                									<td style="text-align: left; padding: 3px 0; font-size: 12px;" valign="middle">
                										Copyright &copy;""" + LocalDateTime.now().getYear() + """ 
                                                        Hobbyzhub. All rights reserved.
                									</td>
                									<td valign="middle" style="padding: 3px 0; font-size: 12px;">
                										<a href="#" target="_blank">{{ domain_info }}</a>
                									</td>
                								</tr>
                							</table>
                							<table>
                								<tr>
                									<td style="font-size: 12px; padding: 0 0 20px;">
                										This email contains sensitive, confidential and trade secret information; and no
                										part of it shall be disclosed to third parties or reproduced in any form or by
                										any electronic or mechanical means, including without limitation information
                										storage and retrieval systems, without the express prior written consent of
                										Hobbyzhub.
                									</td>
                								</tr>
                							</table>
                						</div>
                					</div>
                				</td>
                			</tr>
                		</table>
                	</body>
                </html>             
                """;
    }
}
