sudo docker run --rm \
    -p 8080:8080 \
    -p 50000:50000 \
    -v /home/jenkins:/var/jenkins_home \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -u root\
    --name jenkins codeneko/jenkins-with-docker
