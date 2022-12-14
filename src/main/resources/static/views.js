import {writeNavigation} from './controls/navigator.js';

export function writeDataBlockContainer(){

            let body = document.querySelector('#body');

            body.append(writeNavigation(window.navigate));

            const siteList = window.clientData.siteList;
            let appHeader = document.querySelector('#app-header-placeholder');
            let mainContainer = document.createElement('div');
            mainContainer.className ="container";
            mainContainer.id = "current-view"
            mainContainer.style = "animation: 5s show ease;"



            for(let siteObj of siteList){
                let siteName = siteObj.name;
                console.log("Site name " + siteName);
                let dataList = window.clientData.dataMap[siteName];
                if(dataList == undefined){
                    continue;
                }

                let siteTitleBox = document.createElement('div');
                siteTitleBox.className = "alert mt-3 p-3";

                let siteTitle = document.createElement('h4')
                siteTitle.textContent = siteName.replace('Site', '');

                let siteInfo = document.createElement('p');
                siteInfo.className = "text-muted";
                siteInfo.id = 'site_info'
                siteInfo.textContent = siteObj.title;

                 let siteLink = document.createElement('a');
                 siteLink.className = "link-secondary";
                 siteLink.id = "site_data_link";
                 siteLink.setAttribute( "onclick", "window.navigateClientCatalogData('/app-data-catalog/','" + siteName + "' , " + 0 + ")");
                 siteLink.append(siteTitle);
                 siteLink.append(siteInfo);

                 siteTitleBox.append(siteLink);




                mainContainer.append(siteTitleBox);


                let table = document.createElement('div');
                table.className = "row row-cols-1 row-cols-lg-5 align-items-stretch g-4 py-1";
                table.id = siteName;

                for(let catalogItem of dataList){
                    let coll = document.createElement('div');
                    coll.className = "catalog_item col";
                    let card = document.createElement('div');
                    card.className = "card card-cover h-100 overflow-hidden text-white bg-light bg-gradient rounded-5 shadow-lg";
                    //card.style = "height: 600px;";

                    let dFlex = document.createElement('div');
                    dFlex.className = "d-flex flex-column h-100  pb-3 text-muted text-shadow-1";

                    let link  = document.createElement('a');
                    link.className = "link-secondary";
                    link.id = "item_link";
                    link.href = catalogItem.href;

                    let image = document.createElement('img');
                    image.className = "bd-placeholder-img card-img-top";
                    image.src = catalogItem.imgSrc;
                    image.id = "item_image"
                    link.innerHTML = image.outerHTML;

                    dFlex.append(link);

                    let cardBody = document.createElement('div');
                    cardBody.className = "card-body";

                    let articleDataLink = document.createElement('a');
                    articleDataLink.className = "link-secondary";
                    articleDataLink.id = "article_data_link";
                    articleDataLink.href = "/articles/" + catalogItem.id;

                    let addFavorites = document.createElement('button');
                    //addFavorites.href = appHost + "/app-user-add-favorites/" + catalogItem.id;
                    addFavorites.setAttribute('onclick', "window.addUserFavorites('/app-user-add-favorites', " + catalogItem.id +")");
                    addFavorites.id = "favorite-id-" + catalogItem.id;
                    addFavorites.textContent  = "View later";
                    addFavorites.className = "btn btn-secondary btn-sm  me-2 mb-2";
                    if(window.existsFavorite(catalogItem.id)){
                         addFavorites.disabled = true;
                    }
                    cardBody.append(addFavorites);


                    let date = document.createElement('b');
                    date.className = "card-text text-muted mb-1";
                    date.id = "item_date";
                    date.textContent = catalogItem.postDate;
                    cardBody.append(date);



                    let title = document.createElement('h6');
                    title.className = "card-title";
                    //title.style = "max-width: 300px; display:block;"
                    title.id = "item_title";
                    title.textContent = catalogItem.title;
                    articleDataLink.innerHTML = title.outerHTML;
                    cardBody.append(articleDataLink);

                    dFlex.append(cardBody);
                    card.append(dFlex);
                    coll.append(card);
                    table.append(coll);
                }
                mainContainer.append(table);
            }

            appHeader.after(mainContainer);
        }
        
