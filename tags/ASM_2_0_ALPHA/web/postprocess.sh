#!/bin/sh

cd doc
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-holder.html > tutorial.html
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-holder_print.html > tutorial_print.html

sed -e '/TOBEREPLACED/r tutorial-annotations-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-annotations-holder.html > tutorial-annotations.html
sed -e '/TOBEREPLACED/r tutorial-annotations-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-annotations-holder_print.html > tutorial-annotations_print.html
