<script type='text/javascript'>
	function notEmpty(elem){
		if(elem.value.length == 0){
			return false;
		}
		return true;
	}

	function isNumeric(elem){
		var numericExpression = /^[0-9]+$/;
		if(elem.value.match(numericExpression)){
			return true;
		} else {
			return false;
		}
	}

	function isAlphabet(elem){
        var alphaExp = /^[a-zA-Z0-9\u00A1-\uFFFF\_ .-@]+$/;
        if(elem.value.match(alphaExp)){
            return true;
        } else {
            return false;
        }
    }

    function isAlphanumeric(elem){
        var alphaExp = /^[a-zA-Z0-9\u00A1-\uFFFF\_ .-@]+$/;
        if(elem.value.match(alphaExp) && !isNumeric(elem)){
            return true;
        } else {
            return false;
        }
    }

	function isFloat(elem){
   		if(elem.value.indexOf(".") < 0){
     		return false;
   		} else {
      		if(parseFloat(elem.value)) {
              return true;
          	} else {
              return false;
          	}
   		}
	}

	function taskFormValidator() {
		var i=0;
		var myInputs = new Array();
					myInputs[i] = document.getElementById("serviceDescription");
					i++;
					myInputs[i] = document.getElementById("isContractDefined");
					i++;
					myInputs[i] = document.getElementById("isPolicyFulfilled");
					i++;
					myInputs[i] = document.getElementById("passedIntegrationTestsWithComponents");
					i++;
					myInputs[i] = document.getElementById("passedIntegrationTestsWithServiceConsumers");
					i++;
					myInputs[i] = document.getElementById("isServiceDeployedInProduction");
					i++;
					myInputs[i] = document.getElementById("isServiceConfigUpdated");
					i++;
					myInputs[i] = document.getElementById("consumersInformedAboutDeprecation");
					i++;
					myInputs[i] = document.getElementById("consumersInformedAboutRetirement");
					i++;
					myInputs[i] = document.getElementById("ServiceState");
					i++;
					myInputs[i] = document.getElementById("isDocumentationWritten");
					i++;
					myInputs[i] = document.getElementById("isServiceCreated");
					i++;
					myInputs[i] = document.getElementById("username");
					i++;
					myInputs[i] = document.getElementById("hostname");
					i++;
					myInputs[i] = document.getElementById("port");
					i++;
					myInputs[i] = document.getElementById("password");
					i++;
					myInputs[i] = document.getElementById("serviceSRAMPUUID");
					i++;


		var j=0;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid serviceDescription");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid isContractDefined");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid isPolicyFulfilled");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid passedIntegrationTestsWithComponents");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid passedIntegrationTestsWithServiceConsumers");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid isServiceDeployedInProduction");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid isServiceConfigUpdated");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid consumersInformedAboutDeprecation");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid consumersInformedAboutRetirement");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid ServiceState");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid isDocumentationWritten");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid isServiceCreated");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid username");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid hostname");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isNumeric(myInputs[j])) {
							alert("Please enter valid port");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid password");
							myInputs[j].focus();
							return false;
						}
					j++;
						if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
							alert("Please enter valid serviceSRAMPUUID");
							myInputs[j].focus();
							return false;
						}
					j++;

		return true;
	}
</script>
<style type="text/css">
	#container
	{
		margin: 0 auto;
		width: 600px;
		background:#fff;
	}

	#header
	{
		background: #ccc;
		padding: 20px;
		font-family:Arial, Helvetica, sans-serif;
		font-size: 125%;
		letter-spacing: -1px;
		font-weight: bold;
		line-height: 1.1;
		color:#666;
	}

	#header h1 { margin: 0; }

	#content
	{
		clear: left;
		padding: 20px;
	}

	#content h2
	{
		color: #000;
		font-size: 160%;
		margin: 0 0 .5em;
	}

	#footer
	{
		background: #ccc;
		text-align: right;
		padding: 20px;
		height: 1%;
	}

	fieldset {
		border:1px dashed #CCC;
		padding:10px;
		margin-top:20px;
		margin-bottom:20px;
	}
	legend {
		font-family:Arial, Helvetica, sans-serif;
		font-size: 90%;
		letter-spacing: -1px;
		font-weight: bold;
		line-height: 1.1;
		color:#fff;
		background: #666;
		border: 1px solid #333;
		padding: 2px 6px;
	}
	.form {
		margin:0;
		padding:0;
	}
	label {
		width:140px;
		height:32px;
		margin-top:3px;
		margin-right:2px;
		padding-top:11px;
		padding-left:6px;
		background-color:#CCCCCC;
		float:left;
		display: block;
		font-family:Arial, Helvetica, sans-serif;
		font-size: 115%;
		letter-spacing: -1px;
		font-weight: normal;
		line-height: 1.1;
		color:#666;
	}
	.div_texbox {
		width:347px;
		float:right;
		background-color:#E6E6E6;
		height:35px;
		margin-top:3px;
		padding-top:5px;
		padding-bottom:3px;
		padding-left:5px;
	}
	.div_checkbox {
		width:347px;
		float:right;
		background-color:#E6E6E6;
		height:35px;
		margin-top:3px;
		padding-top:5px;
		padding-bottom:3px;
		padding-left:5px;
	}
	.textbox {
		background-color:#FFFFFF;
		background-repeat: no-repeat;
		background-position:left;
		width:285px;
		font:normal 18px Arial;
		color: #999999;
		padding:3px 5px 3px 19px;
	}
	.checkbox {
		background-color:#FFFFFF;
		background-repeat: no-repeat;
		background-position:left;
		width:285px;
		font:normal 18px Arial;
		color: #999999;
		padding:3px 5px 3px 19px;
	}
	.textbox:focus, .textbox:hover {
		background-color:#F0FFE6;
	}
	.button_div {
		width:287px;
		float:right;
		background-color:#fff;
		border:1px solid #ccc;
		text-align:right;
		height:35px;
		margin-top:3px;
		padding:5px 32px 3px;
	}
	.buttons {
		background: #e3e3db;
		font-size:12px; 
		color: #989070; 
		padding: 6px 14px;
		border-width: 2px;
		border-style: solid;
		border-color: #fff #d8d8d0 #d8d8d0 #fff;
		text-decoration: none;
		text-transform:uppercase;
		font-weight:bold;
	}
</style>
<div id="container">
	<div id="header">
		New Process Instance: /Service Lifecycle Project/src/main/resources.Service Lifecycle Process
	</div>
	<div id="content">
	    <input type="hidden" name="processId" value="${process.id}"/>
		<fieldset>
            <legend>Process inputs</legend>
                            		<label for="name">serviceDescription</label>
                            		<div class="div_texbox">
                              		<input name="serviceDescription" type="text" class="textbox" id="serviceDescription" value="" />
                            		</div>
              	
                            		<label for="name">isContractDefined</label>
                            		<div class="div_checkbox">
                              		<input name="isContractDefined" type="checkbox" class="checkbox" id="isContractDefined" value="true" />
                            		</div>
              	
                            		<label for="name">isPolicyFulfilled</label>
                            		<div class="div_checkbox">
                              		<input name="isPolicyFulfilled" type="checkbox" class="checkbox" id="isPolicyFulfilled" value="true" />
                            		</div>
              	
                            		<label for="name">passedIntegrationTestsWithComponents</label>
                            		<div class="div_checkbox">
                              		<input name="passedIntegrationTestsWithComponents" type="checkbox" class="checkbox" id="passedIntegrationTestsWithComponents" value="true" />
                            		</div>
              	
                            		<label for="name">passedIntegrationTestsWithServiceConsumers</label>
                            		<div class="div_checkbox">
                              		<input name="passedIntegrationTestsWithServiceConsumers" type="checkbox" class="checkbox" id="passedIntegrationTestsWithServiceConsumers" value="true" />
                            		</div>
              	
                            		<label for="name">isServiceDeployedInProduction</label>
                            		<div class="div_checkbox">
                              		<input name="isServiceDeployedInProduction" type="checkbox" class="checkbox" id="isServiceDeployedInProduction" value="true" />
                            		</div>
              	
                            		<label for="name">isServiceConfigUpdated</label>
                            		<div class="div_checkbox">
                              		<input name="isServiceConfigUpdated" type="checkbox" class="checkbox" id="isServiceConfigUpdated" value="true" />
                            		</div>
              	
                            		<label for="name">consumersInformedAboutDeprecation</label>
                            		<div class="div_checkbox">
                              		<input name="consumersInformedAboutDeprecation" type="checkbox" class="checkbox" id="consumersInformedAboutDeprecation" value="true" />
                            		</div>
              	
                            		<label for="name">consumersInformedAboutRetirement</label>
                            		<div class="div_checkbox">
                              		<input name="consumersInformedAboutRetirement" type="checkbox" class="checkbox" id="consumersInformedAboutRetirement" value="true" />
                            		</div>
              	
                            		<label for="name">ServiceState</label>
                            		<div class="div_texbox">
                              		<input name="ServiceState" type="text" class="textbox" id="ServiceState" value="" />
                            		</div>
              	
                            		<label for="name">isDocumentationWritten</label>
                            		<div class="div_checkbox">
                              		<input name="isDocumentationWritten" type="checkbox" class="checkbox" id="isDocumentationWritten" value="true" />
                            		</div>
              	
                            		<label for="name">isServiceCreated</label>
                            		<div class="div_checkbox">
                              		<input name="isServiceCreated" type="checkbox" class="checkbox" id="isServiceCreated" value="true" />
                            		</div>
              	
                            		<label for="name">username</label>
                            		<div class="div_texbox">
                              		<input name="username" type="text" class="textbox" id="username" value="" />
                            		</div>
              	
                            		<label for="name">hostname</label>
                            		<div class="div_texbox">
                              		<input name="hostname" type="text" class="textbox" id="hostname" value="" />
                            		</div>
              	
                            		<label for="name">port</label>
                            		<div class="div_texbox">
                              		<input name="port" type="text" class="textbox" id="port" value="" />
                            		</div>
              	
                            		<label for="name">password</label>
                            		<div class="div_texbox">
                              		<input name="password" type="text" class="textbox" id="password" value="" />
                            		</div>
              	
                            		<label for="name">serviceSRAMPUUID</label>
                            		<div class="div_texbox">
                              		<input name="serviceSRAMPUUID" type="text" class="textbox" id="serviceSRAMPUUID" value="" />
                            		</div>
              	

          </fieldset>
	</div>
	<div id="footer">
	</div>
</div>