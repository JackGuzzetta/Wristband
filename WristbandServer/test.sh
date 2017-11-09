forever stopall
echo "stopped server"
git pull
echo "downloading files"
forever start index.js
echo "started server"
