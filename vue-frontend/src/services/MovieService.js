import axios from 'axios'

const MOVIE_API_BASE_URL_ADMIN ='http://localhost:9000/admin/movie'
const MOVIE_API_BASE_URL='http://localhost:9000/movie'

class MovieService{
    getTop20(){
        return axios.get(MOVIE_API_BASE_URL+"/top")
    }
    getMovies(){
        return axios.get(MOVIE_API_BASE_URL)
    }
    createMovie(data){
        return axios.post(MOVIE_API_BASE_URL_ADMIN,data.movie)
    }
    uploadFile(id, file){
        let URL = MOVIE_API_BASE_URL_ADMIN+'/'+id+'/upload'
        return axios.putForm(URL, file)
    }
    getMovieData(id){
        let URL = MOVIE_API_BASE_URL+'/'+id
        return axios.get(URL);
    }
    updateMovie(id, data){
        let URL=MOVIE_API_BASE_URL_ADMIN+'/'+ id
        return axios.put(URL, data.movie)
    }
    deleteMovie(id){
        let URL =MOVIE_API_BASE_URL_ADMIN+'/'+ id
        return axios.delete(URL)
    }
}

export default new MovieService()