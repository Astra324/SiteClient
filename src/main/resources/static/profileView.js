
export function writeUserProfile(){
    const userProfile = window.userProfile;
    let appHeader = document.querySelector('#app-header-placeholder');

    let mainContainer = document.querySelector('#current-view');
    window.lastViewed = mainContainer.outerHTML;
    mainContainer.innerHTML = "";

    mainContainer.append(writeControls());

    let userDetails = document.createElement('div');
    userDetails.className = 'alert bg-light';
    userDetails.id = 'user-details';

    let name = userProfile.user.username;
    let alias = name.substring(name.indexOf('@') + 1, name.length); //registerDate



    let userName = document.createElement('h6');
    userName.className = "text-muted m-1";
    userName.textContent = " User name : " + name;

    let registerDate = userProfile.user.registerDate;
    let date = document.createElement('p')
    date.className = "text-muted m-1";
    date.textContent = 'Registered date : ' + registerDate;
    

    userDetails.append(userName);
    userDetails.append(date);
    userDetails.append(writeAliasChangeControl(alias));

    let sitesCheckList = document.createElement('ul');
    sitesCheckList.id = 'sites-check-list'
    sitesCheckList.className = 'data-element list-group';
    let index = 0;

    for(let site of userProfile.defaultSitesList){
        let item = document.createElement('li');
        item.className = 'list-group-item';

        let input = document.createElement('input');
        input.className = 'form-check-input me-1';
        input.id = site.name;
        input.setAttribute('type', 'checkbox');
        input.value = userProfile.user.sites[index];
        if(input.value >= 0){
            input.setAttribute('checked', true);
        }
        input.setAttribute('onclick', 'onSiteCheckListItemClick(this, ' + index + ')');

        let label = document.createElement('label');
        label.className = 'form-check-label';
        if(input.value < 0) label.className = 'form-check-label text-muted';
        label.setAttribute('for', site.name);
        label.textContent = site.name;


        item.append(input);
        item.append(label);

        sitesCheckList.append(item);


        console.log(site.name + " state " + userProfile.user.sites[index]);

        index++;
    }



    mainContainer.append(userDetails);
    mainContainer.append(sitesCheckList);
    appHeader.after(mainContainer);

    window.disableElement("user-alias", true);
    
}


window.onSiteCheckListItemClick = function onSiteCheckListItemClick(input, index){
    const userProfile = window.userProfile;
    let list = document.querySelector('#sites-check-list');
    let label = document.querySelector('[for=' + input.id +']');
    if(input.checked == true){
        input.setAttribute('checked', true);
        input.value = index;
        userProfile.user.sites[index] = index;
        label.className = 'form-check-label';
    }else{
        input.removeAttribute('checked');
        input.value = -1;
        userProfile.user.sites[index] = -1;
        label.className = 'form-check-label  text-muted';
    }
    list.setAttribute('changed', 'true');
}

window.onChangeAliasEnabled =  function onChangeAliasEnabled(){
    window.disableElement("user-alias", false)
    let btnChange = document.querySelector('#button-alias');
    btnChange.textContent = 'Apply';
    btnChange.setAttribute('onclick', 'applyAlias()');

}
window.applyAlias = function applyAlias(){
    const userProfile = window.userProfile;
    let userAlias = document.querySelector('#user-alias');
    let newAlias = userAlias.value;
    let name = userProfile.user.username;
    let alias = name.substring(name.indexOf('@') + 1, name.length); //registerDate
    
    if(newAlias.length > 0 && newAlias != alias){
        window.userProfile.userAlias = newAlias;
        //window.userAlias = newAlias;
        userAlias.setAttribute('changed', 'true');
        console.log('changed alias : ' + window.userAlias + ' length : ' + newAlias.length);
    }
        window.disableElement("user-alias", true);
        let btnChange = document.querySelector('#button-alias');
        btnChange.textContent = 'Change';
        btnChange.setAttribute('onclick', 'onChangeAliasEnabled()');




}
window.updateUserProfile = function updateUserProfile() {
    let backBtn = document.querySelector("#button-back");
    let dataElements = document.getElementsByClassName('data-element');
    let isNeedUpdate = false;
    for( let updData of dataElements ){
        let isChanged = updData.getAttribute('changed');
        if(isChanged === 'true'){
        let profileData = updData.getAttribute('data');     
            console.log("new changed value in : " + updData.value+ " data source : " + profileData);
            isNeedUpdate = true;
        }  
    }
    if(isNeedUpdate == true){
        console.log('Profile needed update ' + isNeedUpdate);
        tryUpdateUserProfile('/app-user-profiler-update');
        backBtn.setAttribute('onclick', 'reLogin()');
        for( let data of dataElements ){
            data.setAttribute('changed', 'false');
        }
    }
}

async function tryUpdateUserProfile(url){ ///app-user-profiler-update
                let response = await fetch(url, {
                    method: 'POST',
                    body: JSON.stringify(window.userProfile),
                    headers: {
                      'Content-Type': 'application/json;charset=utf-8',
                      'username' : window.userName
                    },
                  });

                  if (response.ok === true) {
                    window.userProfile = await response.json();
                    window.userName = window.userProfile.user.username;
                    window.setCookie(window.userName);
                    console.log('updated user : ' + window.userName);
                  }
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
    saveBtn.setAttribute('onclick', 'window.updateUserProfile()');
    saveBtn.style = "width: 50px"

    controlContainer.append(backBtn);
    controlContainer.append(saveBtn);

    return controlContainer;
}
function writeAliasChangeControl(alias){
    
    let userAlias = document.createElement('div');
    userAlias.className = "input-group mb-3";
    userAlias.innerHTML = `
    <div class="input-group mb-3"> <span class="input-group-text">User alias : </span>

        <input type="text" class="data-element form-control" placeholder="User alias" 
            aria-label="User alias" aria-describedby="button-addon2" value="${alias}" id="user-alias" data="window.userProfile.user.userAlias">

        <button class="btn btn-outline-secondary" type="button" id="button-alias" onclick='onChangeAliasEnabled()' >Change</button>
    </div>`;

    
    return userAlias;
}
