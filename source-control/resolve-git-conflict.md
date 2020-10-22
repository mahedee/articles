## Resolve git conflict using meld

* [Install meld in windows](https://meldmerge.org/)
* Configure meld in your pc
    * Edit gitconfig file in C:\Users\<username>\
    * Add following line in the gitconfig file
    ```text
    [diff]
	    tool = meld
    [difftool "meld"]  
	    path = C:/Program Files (x86)/Meld/Meld.exe  
    [difftool]
	    prompt = false

    ``` 
* Now check and resolve conflict using the following command
```github
$ git difftool origin/master
```


### Refereces
* [Git Tutorial 7: Diff and Merge using meld 2019](https://www.youtube.com/watch?v=4BqEXdI-Km0)