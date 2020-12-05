from flask import Flask,jsonify
from flask_restful import Api,Resource,reqparse

app = Flask(__name__)
api = Api(app)

parserget = reqparse.RequestParser()

parserpost = reqparse.RequestParser()
parserpost.add_argument('title')
parserpost.add_argument('year')
parserpost.add_argument('description')


MovieDB = []

LastID = 0

class Movies(Resource):
    def get(self):
        global MovieDB
        return MovieDB

    def post(self):
        global parserpost,LastID
        LastID+=1
        args = parserpost.parse_args()
        movie={}
        movie["title"]=args['title']
        movie["year"]=args['year']
        movie["description"]=args['description']
        movie["id"]=LastID
        MovieDB.insert(LastID,movie)
        return {"id":LastID}

    @app.route('/<id>',methods=['DELETE'])
    def delete(id):
        return "{'error':False}"

    @app.route('/<id>',methods=['PUT'])
    def put(id):
        m = MovieDB.index(int(id))
        print (m)
        return "{'error':False}"



def seed(howmany):
    global LastID
    for i in range(0,howmany):
        movie = {'id':LastID,'title':"A movie",'description':"A description",'year':"A year",'rate':5}
        MovieDB.append(movie)
        LastID+=1

api.add_resource(Movies,'/')

if __name__ == '__main__':
    print ('starting REST server...')
    seed(2)
    app.run(host='0.0.0.0')
