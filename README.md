# Movie Fun!

Smoke Tests require server running on port 8080 by default.

## Build WAR ignoring Smoke Tests

```
$ mvn clean package -DskipTests -Dmaven.test.skip=true
```

## Run Smoke Tests against specific URL

```
$ MOVIE_FUN_URL=http://moviefun.example.com mvn test
```
 1259  cd ~/workspace
 1260  git clone https://github.com/nagavejju/apps-movie-fun-code.git
 1261  cd apps-movie-fun-code/
 1262  git branch
 1263  git checkout logging-start
 1264  git checkout -b logging-start-in
 1265  git reset --hard refs/tags/logging-start
 1266  git status
 1267  git commit -am "inital work"
 1269  git push --set-upstream origin logging-start-in
 1270  clear
 1271  history
