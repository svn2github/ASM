#!/bin/sh

cd doc
sed -e '/TOBEREPLACED/r tutorial-asm-2.0-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-asm-2.0-holder.html > tutorial-asm-2.0.html
sed -e '/TOBEREPLACED/r tutorial-asm-2.0-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-asm-2.0-holder_print.html > tutorial-asm-2.0_print.html

sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-holder.html > tutorial.html
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-holder_print.html > tutorial_print.html

sed -e '/TOBEREPLACED/r tutorial-annotations-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-annotations-holder.html > tutorial-annotations.html
sed -e '/TOBEREPLACED/r tutorial-annotations-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' tutorial-annotations-holder_print.html > tutorial-annotations_print.html

sed -e '/TOBEREPLACED/r developer-guide-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' -e 's/<\/head>/<style type="text\/css">pre { margin: 0px 0px 0px 0px; } p, li { margin-right: 0px; text-align: justify; }<\/style><\/head>/' developer-guide-holder.html > developer-guide.html
sed -e '/TOBEREPLACED/r developer-guide-body.html' -e '/TOBEREPLACED/d' -e '/!DOCTYPE/d' -e 's/<\/head>/<style type="text\/css">pre { margin: 0px 0px 0px 0px; } p, li { margin-right: 0px; text-align: justify; }<\/style><\/head>/' developer-guide-holder_print.html > developer-guide_print.html
