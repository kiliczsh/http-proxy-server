echo "starting proxy server"
cd proxy-server/src/
javac *.java -d proxy-server/out/production/proxy-server
cd proxy-server/out/production/proxy-server/
java ProxyMain