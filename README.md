# pio-template-kmeans-clustering-ext

PredictionIO template clustering 2D points. 

## Docker Part

docker pull goliasz/docker-predictionio<br>
cd $HOME<br>
mkdir MyEngine<br>
docker run --hostname pio1 --privileged=true --name pio1 -it -p 8000:8000 -p 7070:7070 -v $HOME/MyEngine:/MyEngine goliasz/docker-predictionio /bin/bash<br>

## PIO Part

root@pio1:/# pio-start-all<br>
root@pio1:/# cd MyEngine<br>
root@pio1:/MyEngine# pio template get goliasz/pio-template-kmeans-clustering-ext --version "0.3" geoclus1<br>
root@pio1:/MyEngine# cd geoclus1
root@pio1:/MyEngine/geoclus1# vi engine.json<br>
Set application name to “geoclus1”<br>
<br>
root@pio1:/MyEngine/geoclus1# pio build --verbose<br>
root@pio1:/MyEngine/geoclus1# pio app new geoclus1<br>
root@pio1:/MyEngine/geoclus1# sh ./data/import.sh [YOUR APP ID from "pio app new textsim" output]<br>
root@pio1:/MyEngine/geoclus1# pio train<br>
root@pio1:/MyEngine/geoclus1# pio deploy --port 8000 &<br>
