# handin tools

handin-check:
	@if ! test -d .git; then \
		echo No .git directory, is this a git repository?; \
		false; \
	fi
	@git branch; \
		read -p "You are going to hand-in the current branch '$(shell git symbolic-ref --short HEAD)'. Is it correct? [y/N] " r; \
		test "$$r" = y;
	@if ! git diff-files --quiet || ! git diff-index --quiet --cached HEAD; then \
		git status -s; \
		echo; \
		echo "You have uncomitted changes.  Please commit or stash them."; \
		false; \
	fi
	@if test -n "`git status -s`"; then \
		git status -s; \
		read -p "Untracked files will not be handed in.  Continue? [y/N] " r; \
		test "$$r" = y; \
	fi

tarball: handin-check
	git archive --format=tar HEAD > Project$(PROJECT)-handin.tar
	tar rf Project$(PROJECT)-handin.tar .git
	gzip -c Project$(PROJECT)-handin.tar > Project$(PROJECT)-handin.tar.gz
	rm Project$(PROJECT)-handin.tar
	@echo
	@echo A compressed tarball of your submission is generated at Project$(PROJECT)-handin.tar.gz

.myapi.key:
	@echo Enter your API key. You should receive it by email or by blackboard.
	@read -p "Please enter your API key: " k; \
	if test `echo "$$k" |tr -d '\n' |wc -c` = 32 ; then \
		TF=`mktemp -t tmp.XXXXXX`; \
		if test "x$$TF" != "x" ; then \
			echo "$$k" |tr -d '\n' > $$TF; \
			mv -f $$TF $@; \
		else \
			echo mktemp failed; \
			false; \
		fi; \
	else \
		echo Bad API key: $$k; \
		echo An API key should be 32 characters long.; \
		false; \
	fi;

handin: tarball .myapi.key
	@echo
	@curl --fail-with-body --connect-timeout 10 --expect100-timeout 20 -F file=@Project$(PROJECT)-handin.tar.gz -H "X-APIKEY: $(shell cat .myapi.key)" $(HANDIN_SERVER) || { echo ; echo ; echo Submit seems to have failed.; echo Please upload the tarball to blackboard directly.; }
