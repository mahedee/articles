## Branching and merging in GitHub

* **To create a branch**
```github
$ git checkout -b branch01
Switched to a new branch 'branch01'
or 
$git branch branch01
```
* **To switch to a branch**  
```github
$git checkout <branch name>

Example
$git checkout branch03
```

* **Push files to the a branch**  
First switch to the specific branch. Then add or modify files. To add files for the commit run the following command.
```github
$ git add .
```

```github
$ git push origin <branch name>

Example:
$ git push origin branch01
```

* **Merge branch01 with master branch**  
```github
$ git checkout master
Switched to branch 'master'
Your branch is up-to-date with 'origin/master'.
```

```github
$ git merge branch01
```

```github
$ git commit -m "merge branch01"
```

```github
$ git push
```


### References
* [Git Branching - Basic Branching and Merging](https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging)
