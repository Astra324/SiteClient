   import {writeDataBlockContainer} from './views.js'; 
   import {writeUserProfile} from './profileView.js';
   const appHost = "http://192.168.1.110:8080";

   export async function loadClientData(data_source, startIndex){
            var url = appHost + data_source + "/" + startIndex + "/" + document.querySelector("#user-name").value;
            console.log(url);

            const response = await fetch(url, {
                method: "GET",
                headers: { "Accept": "application/json" }
            });

            if (response.ok === true) {

                const data = await response.json();
                window.userName = document.querySelector("#user-name").value;
                window.currentIndex = data.currentIndex;
                window.maxResultCount = data.maxResultCount;
                window.dataSource = data.dataSource;
                window.clientData = data;
                window.siteMap = data.clientSiteMap;
                console.log(data + ' \n userbane : ' + window.userName );

                window.setCookie(window.userName);
                writeDataBlockContainer();
            }
   }

   export async function navigateClientData(data_source, startIndex){

            var url = appHost + data_source + "/" + startIndex + "/" + window.userName;
            console.log("data source : " + url + " index : " + startIndex);
            const response = await fetch(url, {
                 method: 'GET',
                 headers: {'Accept': 'application/json'}
            });

            if (response.ok === true) {
                const data = await response.json();
                window.currentIndex = data.currentIndex;
                window.maxResultCount = data.maxResultCount;
                window.dataSource = data.dataSource;
                window.clientData = data;
                window.siteMap = data.clientSiteMap;
                console.log("params : current index : " + window.currentIndex + " data source : " + window.dataSource + " max count : " + maxResultCount);
                let container = document.querySelector('div.container');
                container.remove();
                console.log("User name : " + window.userName);
                writeDataBlockContainer();
                printInfo();
            }
   }
   export async function getUserProfile(url){
                let response = await fetch(url, {
                    method: 'POST',
                    headers: {
                      'Content-Type': 'application/json;charset=utf-8',
                      'username' : window.userName
                    },
                  });

                  if (response.ok === true) {
                    window.userProfile = await response.json();
                    window.userProfile.userAlias = window.userName.substring(window.userName.indexOf('@') + 1, window.userName.length);
                    writeUserProfile();
                    console.log('found user : ' + window.userProfile.user.username);
                  }
   }
   function printInfo(){
                var siteInfo = document.querySelector('#site_info');
                var pages = window.maxResultCount + 1;
                var articles = pages * window.clientData.siteList.length * 15;
                siteInfo.textContent = siteInfo.textContent + ' Found : ' + pages + ' pages ' + articles + ' articles.'
   }

   export function prepareIndex(direction){
                let index = window.currentIndex;
                let maxCount = window.maxResultCount;

                if(direction == 'right'){
                    index++;
                    if(index >= maxCount){
                        index = maxCount;
                    }
                }
                else if(direction == 'left') {
                    if(index > 0){
                        index--;
                    }
                }
                return index;
   }




   