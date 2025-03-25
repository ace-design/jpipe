# jPipe website

## Technological stack

  - github pages
  - Jekyll web framework
  - Minimal Mistake theme


## How to...

### Run locally 

To install the necessary dependencies

```
bundle install
```

To clean up the directory and start fresh

```
bundle exec jekyll clean
```

To run locally

```
bundle exec jekyll serve --incremental
```

### Deploy on Github

Simply `git push` your modifications. It triggers the `.github/workflows/web.yml` Github Actions pipeline to support deployment. 