#!/bin/sh

cd doc
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-holder.html > tutorial.html
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-holder_print.html > tutorial_print.html
