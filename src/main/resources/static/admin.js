const url = 'http://localhost:8080/admin/api/users'

// delete

function deleteModal(id) {
    fetch(url + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(res => {
            res.json()
                .then(u => {
                    document.getElementById('idDelete').value = u.id
                    document.getElementById('usernameDelete').value = u.username
                    document.getElementById('ageDelete').value = u.age
                    document.getElementById('emailDelete').value = u.email
                })
        })
}

async function deleteUser() {
    let id = document.getElementById('idDelete').value
    let username = document.getElementById('usernameDelete').value
    let age = document.getElementById('ageDelete').value
    let email = document.getElementById('emailDelete').value

    let user = {
        id: id,
        username: username,
        age: age,
        email: email
    }

    await fetch(url + '/' + id,  {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(() => {
        $('#deleteModal').modal('hide')
        getAllUsers()
    })

}

// edit

function editModal(id) {
    fetch(url + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(u => {
            document.getElementById('editId').value = u.id
            document.getElementById('editUsername').value = u.username
            document.getElementById('editAge').value = u.age
            document.getElementById('editEmail').value = u.email
            document.getElementById('editPassword').value = u.password
            document.getElementById('editRoles').selectedIndex = u.role
        })
    })
}

async function editUser() {
    const form_ed = document.getElementById('editModalForm')
    let id = document.getElementById('editId').value
    let username = document.getElementById('editUsername').value
    let age = document.getElementById('editAge').value
    let email = document.getElementById('editEmail').value
    let password = document.getElementById('editPassword').value
    let roles = []
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            let tmp = {}
            tmp['id'] = form_ed.roles.options[i].value
            roles.push(tmp)
        }
    }

    let user = {
        id: id,
        username: username,
        age: age,
        email: email,
        password: password,
        roles: roles
    }

    await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(() => {
        $('#editModal').modal('hide');
        getAllUsers();
    })
}

// новый список всех
const usersList = document.getElementById('users')


const renderUsers = (users) => {
    let output = ''

    for(let user of users) {
        output += `
                                        <tr>
                                            <td data-id="${user.id}">${user.id}</td>
                                            <td>${user.username}</td>
                                            <td>${user.age}</td>
                                            <td>${user.email}</td>
                                            <td>${user.roles.map(r => r.name).join(', ')}</td>
                                            <td>
                                               
                                                <button 
                                                id="edit-user" 
                                                type="button" 
                                                class="btn btn-info" 
                                                data-toggle="modal" 
                                                data-target="#editModal"
                                                onclick="event.preventDefault(); editModal(${user.id})">
                                                    Edit
                                                </button>
                                            </td>
                                            <td>
                                                <!-- Button trigger modal -->
                                                <button 
                                                id="delete-user" 
                                                type="button" 
                                                class="btn btn-danger" 
                                                data-toggle="modal" 
                                                data-target="#deleteModal"
                                                onclick="event.preventDefault(); deleteModal(${user.id})">
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>`
    }
    document.getElementById('users').innerHTML = output
}
async function getAllUsers() {
    setTimeout(() => {
        fetch(url)
            .then(res => res.json())
            .then(data => renderUsers(data))
    }, 200)

}




// добавление нового пользователя

async function addUser() {
    const form_ed = document.getElementById('formAdd');
    let addUserName = document.getElementById("usernameAdd").value;
    let addAge = document.getElementById("ageAdd").value;
    let addEmail = document.getElementById("emailAdd").value;
    let addPassword = document.getElementById("passwordAdd").value;
    let addRoles = [];
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            let tmp = {};
            tmp["id"] = form_ed.roles.options[i].value
            addRoles.push(tmp);
        }
    }
    let user = {
        username: addUserName,
        age: addAge,
        email: addEmail,
        password: addPassword,
        roles: addRoles
    }
    await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    }).then(() => {
        clearAndHideAddForm();
        getAllUsers()
        window.location.reload();
    })
}


function clearAndHideAddForm() {
    document.getElementById("usernameAdd").value = "";
    document.getElementById("emailAdd").value = "";
    document.getElementById("passwordAdd").value = "";
    document.getElementById("ageAdd").value = "";
}
