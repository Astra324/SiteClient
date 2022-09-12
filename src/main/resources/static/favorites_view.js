import {writeNavigation} from './controls/navigator.js';

export function writeFavoritesBlockContainer(){
    const favMap = window.favoritesMap;
    let body = document.querySelector('#body');
    body.append(writeNavigation(window.navigateFavorites));

    let appHeader = document.querySelector('#app-header-placeholder');

    let mainContainer = document.querySelector('#current-view');
    window.lastViewed = mainContainer.outerHTML;
    mainContainer.innerHTML = "";

    mainContainer.append(writeControls());

    let listContainer = document.createElement('div');

    for(let key in favMap){
      listContainer.append(writeBlockListAlt(key, favMap[key]));   
    }

    mainContainer.append(listContainer);
    appHeader.after(mainContainer);

}
function writeBlockListAlt(title, items){
  let listContent = "";
  for(let item of items){
    listContent = listContent + writeItem(item);
  }
  let listBlock = document.createElement('div');
  listBlock.innerHTML = `<div class="my-3 p-3 bg-body rounded shadow-sm">
                          <h6 class="border-bottom pb-2 mb-0">${title}</h6>
                          ${listContent}
                        </div>
                        `;
    return listBlock;
}

function writeItem(item){

let html = `
          <div class="d-flex text-muted pt-3">
            <img src="${item.imgSrc}" class="rounded me-3 ratio ratio-16x9" alt="..." style="max-height:150px; max-width:200px">
            <a href=${window.appHost}/articles/${item.id} class="link-secondary">
            <p class="pb-3 mb-0 middle lh-sm border-bottom">
            ${item.title}
            <strong class="d-block text-gray-dark mt-2">${item.postDate}</strong>
            </p>
            </a>

          </div>`;
  return html;
}

function writeControls(){
    let controlContainer = document.createElement('div');
    controlContainer.className = "alert btn-group m-3";
    controlContainer.id = "profile-control";

    let backBtn = document.createElement('button');
    backBtn.className = "btn btn-sm btn-outline-secondary";
    backBtn.setAttribute('onclick', 'window.reLogin()');
    backBtn.textContent = " Back ";
    backBtn.id = "button-back";
    backBtn.style = "width: 50px"

    let saveBtn = document.createElement('button');
    saveBtn.className = "btn btn-sm btn-outline-secondary"; // updateUserProfile
    saveBtn.textContent = "Save ";
    saveBtn.setAttribute('onclick', '');
    saveBtn.style = "width: 50px"

    controlContainer.append(backBtn);
    controlContainer.append(saveBtn);

    return controlContainer;
}

