import axios from 'axios'

const ACTOR_API_BASE_URL='http://localhost:9000/actor'
const ACTOR_API_ADMIN_BASE_URL='http://localhost:9000/admin/actor'

class ActorService{

    getActors(){
        return axios.get(ACTOR_API_BASE_URL)
    }
    createActor(data){
        return axios.postForm(ACTOR_API_ADMIN_BASE_URL,data)
    }
    getActorData(id){
        let URL =ACTOR_API_BASE_URL+'/'+id
        return axios.get(URL);
    }
    updateActor(data){
        let URL =ACTOR_API_ADMIN_BASE_URL+'/'+data.id
        return axios.putForm(URL,data);
    }
    deleteActor(id){
        let URL =ACTOR_API_ADMIN_BASE_URL+'/'+id
        return axios.delete(URL)
    }
}

export default new ActorService()