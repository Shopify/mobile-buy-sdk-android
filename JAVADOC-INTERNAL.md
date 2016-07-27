# Updating javadoc on public repo

	git checkout <branch you want javadoc for>
      	cd MobileBuy
    	./gradlew javadoc
    	mv buy/build/docs/ temp
    	git checkout gh-pages
    	git rm -r buy/build/docs
    	mv temp buy/build/docs
    	git add buy/build/docs/*
    	git commit -m 'update javadoc'
    	git push
