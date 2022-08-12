   import {writeDataBlockContainer} from './views.js';
   //const appHost = "http://localhost:8080";
   const appHost = "http://192.168.1.104:8080";

   export async function loadClientData(data_source, startIndex){

            var url = appHost + data_source + "/" + startIndex;

            console.log(url);

            const response = await fetch(url, {
                method: "GET",
                headers: { "Accept": "application/json" }
            });

            if (response.ok === true) {
                const data = await response.json();
                window.currentIndex = data.currentIndex;
                window.maxResultCount = data.maxResultCount;
                window.dataSource = data.dataSource;
                window.clientData = data;
                console.log("params : current index : " + window.currentIndex + " data source : " + window.dataSource + " max count : " + maxResultCount);
                writeDataBlockContainer();
            }
   }

   export async function navigateClientData(data_source, startIndex){
            var url = appHost + data_source + "/" + startIndex;
            console.log("data source : " + url + " index : " + startIndex);
            const response = await fetch(url, {
                method: "GET",
                headers: { "Accept": "application/json" }
            });

            if (response.ok === true) {
                const data = await response.json();
                window.currentIndex = data.currentIndex;
                window.maxResultCount = data.maxResultCount;
                window.dataSource = data.dataSource;
                window.clientData = data;
                console.log("params : current index : " + window.currentIndex + " data source : " + window.dataSource + " max count : " + maxResultCount);
                let container = document.querySelector('div.container');
                container.remove();
                writeDataBlockContainer();
                printInfo();
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




   