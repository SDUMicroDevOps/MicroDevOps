import BasicUserManager from './BasicUserManager'
class AdminManager extends BasicUserManager{
    constructor(username, users){
        super(username);
        this.users = users;
    }
}

export default AdminManager;