## Getting Started with GitHub
* [Create a GitHub account.](https://github.com/)
* [Setting up git on your computer](https://docs.github.com/en/free-pro-team@latest/github/getting-started-with-github/set-up-git)

    * [Download and install the latest version of Git.](https://git-scm.com/downloads)  
    * [Set your username in Git.](https://docs.github.com/en/free-pro-team@latest/github/using-git/setting-your-username-in-git)  

* [Create a repository](https://docs.github.com/en/free-pro-team@latest/github/getting-started-with-github/create-a-repo)

* Clone your repository    
For example:  
```github
git clone https://github.com/mahedee/eSchool.git
```

* Pull and push to repository

Pull command uses to get updated files from github repository to your local machine 
```github
git pull --rebase upstream master
or 
git pull
```

Push command uses to sync your local file to github repository

Add files for the commit 
```github
git add .
```
Write a message for the commit
```github
git commit -m "complete feature-joining"
```
```github
git push origin feature-joining
or 
git push
```
