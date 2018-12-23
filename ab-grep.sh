echo "started"
ab -n 10  -c  1  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/10/ok/n10c1-short.txt
ab -n 10  -c  5  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/10/ok/n10c5-short.txt
ab -n 10  -c  10 localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/10/ok/n10c10-short.txt
ab -n 10  -kc 1  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/10/wk/n10kc1-short.txt
ab -n 10  -kc 5  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/10/wk/n10kc5-short.txt
ab -n 10  -kc 10 localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/10/wk/n10kc10-short.txt
ab -n 100 -c  1  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/100/ok/n100c1-short.txt
ab -n 100 -c  5  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/100/ok/n100c5-short.txt
ab -n 100 -c  10 localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/100/ok/n100c10-short.txt
ab -n 100 -kc 1  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/100/wk/n100kc1-short.txt
ab -n 100 -kc 5  localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/100/wk/n100kc5-short.txt
ab -n 100 -kc 10 localhost:5000/100   | grep -i "time \|transfer" > ab/with-proxy/100/100/wk/n100kc10-short.txt
ab -n 10  -c  1  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/10/ok/n10c1-short.txt
ab -n 10  -c  5  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/10/ok/n10c5-short.txt
ab -n 10  -c  10 localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/10/ok/n10c10-short.txt
ab -n 10  -kc 1  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/10/wk/n10kc1-short.txt
ab -n 10  -kc 5  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/10/wk/n10kc5-short.txt
ab -n 10  -kc 10 localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/10/wk/n10kc10-short.txt
ab -n 100 -c  1  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/100/ok/n100c1-short.txt
ab -n 100 -c  5  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/100/ok/n100c5-short.txt
ab -n 100 -c  10 localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/100/ok/n100c10-short.txt
ab -n 100 -kc 1  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/100/wk/n100kc1-short.txt
ab -n 100 -kc 5  localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/100/wk/n100kc5-short.txt
ab -n 100 -kc 10 localhost:5000/20000 | grep -i "time \|transfer" > ab/with-proxy/20000/100/wk/n100kc10-short.txt

echo "end"