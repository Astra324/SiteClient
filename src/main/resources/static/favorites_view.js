export function writeFavoritesBlockContainer(){
    const favMap = window.favoritesMap;
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
console.log('item : ' + item.id);
let html = `
          <div class="d-flex text-muted pt-3">
            <img src="${item.imgSrc}" class="rounded me-3 ratio ratio-16x9" alt="..." style="max-height:150px; max-width:200px">
            <p class="pb-3 mb-0 middle lh-sm border-bottom"> 
            ${item.title}
            <strong class="d-block text-gray-dark mt-2">${item.postDate}</strong>
            </p>
          </div>`;
  return html;
}

function writeControls(){
    let controlContainer = document.createElement('div');
    controlContainer.className = "alert btn-group m-3";
    controlContainer.id = "profile-control";

    let backBtn = document.createElement('button');
    backBtn.className = "btn btn-sm btn-outline-secondary";
    backBtn.setAttribute('onclick', 'window.backStep()');
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

 let content = `<div class="my-3 p-3 bg-body rounded shadow-sm">
                       <h6 class="border-bottom pb-2 mb-0">Recent updates</h6>
                       <div class="d-flex text-muted pt-3">
                         <svg class="bd-placeholder-img flex-shrink-0 me-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: 32x32" preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title><rect width="100%" height="100%" fill="#007bff"></rect><text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text></svg>

                         <p class="pb-3 mb-0 small lh-sm border-bottom">
                           <strong class="d-block text-gray-dark">@username</strong>
                           Some representative placeholder content, with some information about this user. Imagine this being some sort of status update, perhaps?
                         </p>
                       </div>
                       <div class="d-flex text-muted pt-3">
                         <svg class="bd-placeholder-img flex-shrink-0 me-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: 32x32" preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title><rect width="100%" height="100%" fill="#e83e8c"></rect><text x="50%" y="50%" fill="#e83e8c" dy=".3em">32x32</text></svg>

                         <p class="pb-3 mb-0 small lh-sm border-bottom">
                           <strong class="d-block text-gray-dark">@username</strong>
                           Some more representative placeholder content, related to this other user. Another status update, perhaps.
                         </p>
                       </div>
                       <div class="d-flex text-muted pt-3">
                         <svg class="bd-placeholder-img flex-shrink-0 me-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: 32x32" preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title><rect width="100%" height="100%" fill="#6f42c1"></rect><text x="50%" y="50%" fill="#6f42c1" dy=".3em">32x32</text></svg>

                         <p class="pb-3 mb-0 small lh-sm border-bottom">
                           <strong class="d-block text-gray-dark">@username</strong>
                           This user also gets some representative placeholder content. Maybe they did something interesting, and you really want to highlight this in the recent updates.
                         </p>
                       </div>

                     </div>`;