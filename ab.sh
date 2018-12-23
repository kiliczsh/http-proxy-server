
echo "started"

ab -n 10 -c 1 localhost:5000/100    >  ab/without/100/10/ok/n10c1.txt
ab -n 10 -c 5 localhost:5000/100    >  ab/without/100/10/ok/n10c5.txt
ab -n 10 -c 10 localhost:5000/100   >  ab/without/100/10/ok/n10c10.txt


ab -n 10 -kc 1 localhost:5000/100   >  ab/without/100/10/wk/n10kc1.txt
ab -n 10 -kc 5 localhost:5000/100   >  ab/without/100/10/wk/n10kc5.txt
ab -n 10 -kc 10 localhost:5000/100  >  ab/without/100/10/wk/n10kc10.txt

ab -n 100 -c 1 localhost:5000/100   >  ab/without/100/100/ok/n100c1.txt
ab -n 100 -c 5 localhost:5000/100   >  ab/without/100/100/ok/n100c5.txt
ab -n 100 -c 10 localhost:5000/100  >  ab/without/100/100/ok/n100c10.txt

ab -n 100 -kc 1 localhost:5000/100  >  ab/without/100/100/wk/n100kc1.txt
ab -n 100 -kc 5 localhost:5000/100  >  ab/without/100/100/wk/n100kc5.txt
ab -n 100 -kc 10 localhost:5000/100 >  ab/without/100/100/wk/n100kc10.txt

echo "end of 100/100"

echo "start 20000"

ab -n 10 -c 1    localhost:5000/20000 > ab/without/20000/10/ok/n10c1.txt
ab -n 10 -c 5    localhost:5000/20000 > ab/without/20000/10/ok/n10c5.txt
ab -n 10 -c 10   localhost:5000/20000 > ab/without/20000/10/ok/n10c10.txt

ab -n 10 -kc 1   localhost:5000/20000 > ab/without/20000/100/ok/n10kc1.txt
ab -n 10 -kc 5   localhost:5000/20000 > ab/without/20000/100/ok/n10kc5.txt
ab -n 10 -kc 10  localhost:5000/20000 > ab/without/20000/100/ok/n10kc10.txt

ab -n 100 -c 1   localhost:5000/20000 > ab/without/20000/10/wk/n100c1.txt
ab -n 100 -c 5   localhost:5000/20000 > ab/without/20000/10/wk/n100c5.txt
ab -n 100 -c 10  localhost:5000/20000 > ab/without/20000/10/wk/n100c10.txt

ab -n 100 -kc 1  localhost:5000/20000 > ab/without/20000/100/wk/n100kc1.txt
ab -n 100 -kc 5  localhost:5000/20000 > ab/without/20000/100/wk/n100kc5.txt
ab -n 100 -kc 10 localhost:5000/20000 > ab/without/20000/100/wk/n100kc10.txt

echo "end"
